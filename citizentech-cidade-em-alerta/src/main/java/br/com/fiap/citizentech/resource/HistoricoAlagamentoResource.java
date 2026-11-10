package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.HistoricoAlagamentoBO;
import br.com.fiap.citizentech.dto.HistoricoAlagamentoRequest;
import br.com.fiap.citizentech.dto.HistoricoAlagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/historicos-alagamento")
@Tag(name = "Histórico de alagamentos")
public class HistoricoAlagamentoResource {

    private final HistoricoAlagamentoBO historicoBO;

    public HistoricoAlagamentoResource(HistoricoAlagamentoBO historicoBO) {
        this.historicoBO = historicoBO;
    }

    @GetMapping
    @Operation(summary = "Lista os históricos de alagamento")
    public List<HistoricoAlagamentoResponse> listar(@RequestParam(required = false) Long microRegiaoId) {
        return historicoBO.listar(microRegiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o histórico de alagamento por id")
    public HistoricoAlagamentoResponse buscarPorId(@PathVariable Long id) {
        return historicoBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra o histórico de alagamento")
    public ResponseEntity<HistoricoAlagamentoResponse> criar(@Valid @RequestBody HistoricoAlagamentoRequest requisicao) {
        HistoricoAlagamentoResponse criado = historicoBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o histórico de alagamento")
    public HistoricoAlagamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody HistoricoAlagamentoRequest requisicao) {
        return historicoBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui o histórico de alagamento")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        historicoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
