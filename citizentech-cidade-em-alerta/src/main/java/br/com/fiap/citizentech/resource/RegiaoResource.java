package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.RegiaoBO;
import br.com.fiap.citizentech.dto.RegiaoRequest;
import br.com.fiap.citizentech.dto.RegiaoResponse;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/regioes")
@Tag(name = "Regiões")
public class RegiaoResource {

    private final RegiaoBO regiaoBO;

    public RegiaoResource(RegiaoBO regiaoBO) {
        this.regiaoBO = regiaoBO;
    }

    @GetMapping
    @Operation(summary = "Lista as regiões")
    public List<RegiaoResponse> listar() {
        return regiaoBO.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca a região por id")
    public RegiaoResponse buscarPorId(@PathVariable Long id) {
        return regiaoBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra a região")
    public ResponseEntity<RegiaoResponse> criar(@Valid @RequestBody RegiaoRequest requisicao) {
        RegiaoResponse criado = regiaoBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza a região")
    public RegiaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody RegiaoRequest requisicao) {
        return regiaoBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui a região")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        regiaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
