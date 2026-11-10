package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.SensorBO;
import br.com.fiap.citizentech.dto.SensorRequest;
import br.com.fiap.citizentech.dto.SensorResponse;
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
@RequestMapping("/sensores")
@Tag(name = "Sensores")
public class SensorResource {

    private final SensorBO sensorBO;

    public SensorResource(SensorBO sensorBO) {
        this.sensorBO = sensorBO;
    }

    @GetMapping
    @Operation(summary = "Lista os sensores")
    public List<SensorResponse> listar(@RequestParam(required = false) Long microRegiaoId) {
        return sensorBO.listar(microRegiaoId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o sensor por id")
    public SensorResponse buscarPorId(@PathVariable Long id) {
        return sensorBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra o sensor")
    public ResponseEntity<SensorResponse> criar(@Valid @RequestBody SensorRequest requisicao) {
        SensorResponse criado = sensorBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o sensor")
    public SensorResponse atualizar(@PathVariable Long id, @Valid @RequestBody SensorRequest requisicao) {
        return sensorBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui o sensor")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        sensorBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
