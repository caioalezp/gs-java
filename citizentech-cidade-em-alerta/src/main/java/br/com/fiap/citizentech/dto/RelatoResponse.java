package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.Relato;
import br.com.fiap.citizentech.model.enums.StatusRelato;
import br.com.fiap.citizentech.model.enums.TipoRelato;
import java.time.LocalDateTime;

public record RelatoResponse(Long id, Long moradorId, String moradorNome, Long microRegiaoId,
        String microRegiaoNome, TipoRelato tipo, String descricao, LocalDateTime dataHora, StatusRelato status) {

    public static RelatoResponse from(Relato r) {
        return new RelatoResponse(r.getId(), r.getMorador().getId(), r.getMorador().getNome(),
                r.getMicroRegiao().getId(), r.getMicroRegiao().getNome(), r.getTipo(), r.getDescricao(),
                r.getDataHora(), r.getStatus());
    }
}
