package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.AlertaBO;
import br.com.fiap.citizentech.bo.MicroRegiaoBO;
import br.com.fiap.citizentech.dto.MicroRegiaoRequest;
import br.com.fiap.citizentech.dto.MicroRegiaoResponse;
import br.com.fiap.citizentech.dto.PrioridadeResponse;
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
@RequestMapping("/micro-regioes")
@Tag(name = "Microrregiões")
public class MicroRegiaoResource {

    private final MicroRegiaoBO microRegiaoBO;
    private final AlertaBO alertaBO;

    public MicroRegiaoResource(MicroRegiaoBO microRegiaoBO, AlertaBO alertaBO) {
        this.microRegiaoBO = microRegiaoBO;
        this.alertaBO = alertaBO;
    }

    @GetMapping
    @Operation(summary = "Lista as microrregiões")
    public List<MicroRegiaoResponse> listar(@RequestParam(required = false) Long regiaoId) {
        return microRegiaoBO.listar(regiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca a microrregião por id")
    public MicroRegiaoResponse buscarPorId(@PathVariable Long id) {
        return microRegiaoBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra a microrregião")
    public ResponseEntity<MicroRegiaoResponse> criar(@Valid @RequestBody MicroRegiaoRequest requisicao) {
        MicroRegiaoResponse criado = microRegiaoBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza a microrregião")
    public MicroRegiaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody MicroRegiaoRequest requisicao) {
        return microRegiaoBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui a microrregião")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        microRegiaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prioridade")
    @Operation(summary = "Ranking de microrregiões por prioridade de atendimento")
    public List<PrioridadeResponse> prioridade() {
        return alertaBO.prioridade();
    }
}
