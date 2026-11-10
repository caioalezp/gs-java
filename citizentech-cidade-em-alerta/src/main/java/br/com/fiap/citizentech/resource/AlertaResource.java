package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.AlertaBO;
import br.com.fiap.citizentech.dto.AlertaRequest;
import br.com.fiap.citizentech.dto.AlertaResponse;
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
@RequestMapping("/alertas")
@Tag(name = "Alertas")
public class AlertaResource {

    private final AlertaBO alertaBO;

    public AlertaResource(AlertaBO alertaBO) {
        this.alertaBO = alertaBO;
    }

    @GetMapping
    @Operation(summary = "Lista os alertas")
    public List<AlertaResponse> listar(@RequestParam(required = false) Long microRegiaoId) {
        return alertaBO.listar(microRegiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o alerta por id")
    public AlertaResponse buscarPorId(@PathVariable Long id) {
        return alertaBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra o alerta")
    public ResponseEntity<AlertaResponse> criar(@Valid @RequestBody AlertaRequest requisicao) {
        AlertaResponse criado = alertaBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o alerta")
    public AlertaResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlertaRequest requisicao) {
        return alertaBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui o alerta")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alertaBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calcular/{microRegiaoId}")
    @Operation(summary = "Calcula o Alerta Antecipado da microrregião e registra o alerta")
    public ResponseEntity<AlertaResponse> calcular(@PathVariable Long microRegiaoId) {
        AlertaResponse gerado = alertaBO.calcular(microRegiaoId);
        URI local = ServletUriComponentsBuilder.fromCurrentContextPath().path("/alertas/{id}")
                .buildAndExpand(gerado.id()).toUri();
        return ResponseEntity.created(local).body(gerado);
    }

    @PostMapping("/{id}/equipes/{equipeId}")
    @Operation(summary = "Designa uma equipe para atender o alerta")
    public AlertaResponse designarEquipe(@PathVariable Long id, @PathVariable Long equipeId) {
        return alertaBO.designarEquipe(id, equipeId);
    }

    @DeleteMapping("/{id}/equipes/{equipeId}")
    @Operation(summary = "Libera uma equipe do atendimento do alerta")
    public AlertaResponse liberarEquipe(@PathVariable Long id, @PathVariable Long equipeId) {
        return alertaBO.liberarEquipe(id, equipeId);
    }
}
