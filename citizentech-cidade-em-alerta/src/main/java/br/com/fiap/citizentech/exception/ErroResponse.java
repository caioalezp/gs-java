package br.com.fiap.citizentech.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

/** Corpo padronizado de erro devolvido por toda a API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        List<CampoErro> campos) {

    public record CampoErro(String campo, String mensagem) {
    }
}
