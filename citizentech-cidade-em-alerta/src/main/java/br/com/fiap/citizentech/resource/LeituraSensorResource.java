package br.com.fiap.citizentech.resource;

import br.com.fiap.citizentech.bo.LeituraSensorBO;
import br.com.fiap.citizentech.dto.LeituraSensorRequest;
import br.com.fiap.citizentech.dto.LeituraSensorResponse;
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
@RequestMapping("/leituras")
@Tag(name = "Leituras de sensores")
public class LeituraSensorResource {

    private final LeituraSensorBO leituraSensorBO;

    public LeituraSensorResource(LeituraSensorBO leituraSensorBO) {
        this.leituraSensorBO = leituraSensorBO;
    }

    @GetMapping
    @Operation(summary = "Lista as leituras")
    public List<LeituraSensorResponse> listar(@RequestParam(required = false) Long sensorId) {
        return leituraSensorBO.listar(sensorId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca a leitura por id")
    public LeituraSensorResponse buscarPorId(@PathVariable Long id) {
        return leituraSensorBO.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra a leitura")
    public ResponseEntity<LeituraSensorResponse> criar(@Valid @RequestBody LeituraSensorRequest requisicao) {
        LeituraSensorResponse criado = leituraSensorBO.criar(requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza a leitura")
    public LeituraSensorResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeituraSensorRequest requisicao) {
        return leituraSensorBO.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui a leitura")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        leituraSensorBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
