package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.RelatoBO;
import br.com.fiap.citizentech.dto.RelatoRequest;
import br.com.fiap.citizentech.dto.RelatoResponse;
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
@RequestMapping("/relatos")
@Tag(name = "Relatos")
public class RelatoResource {

    private final RelatoBO relatoBO;

    public RelatoResource(RelatoBO relatoBO) {
        this.relatoBO = relatoBO;
    }

    @GetMapping
    @Operation(summary = "Lista os relatos")
    public List<RelatoResponse> listar(@RequestParam(required = false) Long microRegiaoId) {
        return relatoBO.listar(microRegiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o relato por id")
    public RelatoResponse buscarPorId(@PathVariable Long id) {
        return relatoBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra o relato")
    public ResponseEntity<RelatoResponse> criar(@Valid @RequestBody RelatoRequest requisicao) {
        RelatoResponse criado = relatoBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o relato")
    public RelatoResponse atualizar(@PathVariable Long id, @Valid @RequestBody RelatoRequest requisicao) {
        return relatoBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui o relato")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        relatoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
