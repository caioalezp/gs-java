package br.com.fiap.citizentech.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Camada de tratamento de exceções: converte qualquer erro em JSON padronizado (ErroResponse).
 * Estende ResponseEntityExceptionHandler para tratar também os erros do próprio Spring MVC
 * (método não permitido, parâmetro ausente, tipo inválido etc.) com o mesmo formato.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> naoEncontrado(RecursoNaoEncontradoException ex, WebRequest request) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> regraNegocio(RegraNegocioException ex, WebRequest request) {
        return montar(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<Object> conflito(ConflitoException ex, WebRequest request) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> integridade(DataIntegrityViolationException ex, WebRequest request) {
        LOG.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
        return montar(HttpStatus.CONFLICT,
                "Operação viola a integridade dos dados: o registro está referenciado por outros ou o valor já existe.",
                request, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> restricao(ConstraintViolationException ex, WebRequest request) {
        List<ErroResponse.CampoErro> campos = ex.getConstraintViolations().stream()
                .map(v -> new ErroResponse.CampoErro(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos.", request, campos);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generico(Exception ex, WebRequest request) {
        LOG.error("Erro inesperado", ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor. Tente novamente mais tarde.", request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErroResponse.CampoErro> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErroResponse.CampoErro(e.getField(), e.getDefaultMessage()))
                .toList();
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos.", request, campos);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return montar(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido ou malformado (verifique JSON, datas e valores de enumeração).",
                request, null);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return montar(status, mensagemAmigavel(status), request, null);
    }

    private String mensagemAmigavel(HttpStatus status) {
        return switch (status) {
            case METHOD_NOT_ALLOWED -> "Método HTTP não permitido para este recurso.";
            case UNSUPPORTED_MEDIA_TYPE -> "Tipo de conteúdo não suportado. Use application/json.";
            case NOT_FOUND -> "Recurso não encontrado.";
            case BAD_REQUEST -> "Requisição inválida: verifique parâmetros e tipos informados.";
            default -> status.getReasonPhrase();
        };
    }

    private ResponseEntity<Object> montar(HttpStatus status, String mensagem, WebRequest request,
            List<ErroResponse.CampoErro> campos) {
        String caminho = request.getDescription(false).replace("uri=", "");
        ErroResponse corpo = new ErroResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                mensagem, caminho, campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
