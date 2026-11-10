package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.EquipeAtendimentoBO;
import br.com.fiap.citizentech.dto.EquipeAtendimentoRequest;
import br.com.fiap.citizentech.dto.EquipeAtendimentoResponse;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;
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
@RequestMapping("/equipes")
@Tag(name = "Equipes de atendimento")
public class EquipeAtendimentoResource {

    private final EquipeAtendimentoBO equipeBO;

    public EquipeAtendimentoResource(EquipeAtendimentoBO equipeBO) {
        this.equipeBO = equipeBO;
    }

    @GetMapping
    @Operation(summary = "Lista as equipes")
    public List<EquipeAtendimentoResponse> listar(@RequestParam(required = false) DisponibilidadeEquipe disponibilidade) {
        return equipeBO.listar(disponibilidade);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca a equipe por id")
    public EquipeAtendimentoResponse buscarPorId(@PathVariable Long id) {
        return equipeBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra a equipe")
    public ResponseEntity<EquipeAtendimentoResponse> criar(@Valid @RequestBody EquipeAtendimentoRequest requisicao) {
        EquipeAtendimentoResponse criado = equipeBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza a equipe")
    public EquipeAtendimentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody EquipeAtendimentoRequest requisicao) {
        return equipeBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui a equipe")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        equipeBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
