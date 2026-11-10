package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.MoradorBO;
import br.com.fiap.citizentech.dto.MoradorRequest;
import br.com.fiap.citizentech.dto.MoradorResponse;
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
@RequestMapping("/moradores")
@Tag(name = "Moradores")
public class MoradorResource {

    private final MoradorBO moradorBO;

    public MoradorResource(MoradorBO moradorBO) {
        this.moradorBO = moradorBO;
    }

    @GetMapping
    @Operation(summary = "Lista os moradores")
    public List<MoradorResponse> listar(@RequestParam(required = false) Long microRegiaoId) {
        return moradorBO.listar(microRegiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o morador por id")
    public MoradorResponse buscarPorId(@PathVariable Long id) {
        return moradorBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra o morador")
    public ResponseEntity<MoradorResponse> criar(@Valid @RequestBody MoradorRequest requisicao) {
        MoradorResponse criado = moradorBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o morador")
    public MoradorResponse atualizar(@PathVariable Long id, @Valid @RequestBody MoradorRequest requisicao) {
        return moradorBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui o morador")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        moradorBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
