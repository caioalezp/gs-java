package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.HistoricoAlagamento;
import java.time.LocalDate;

public record HistoricoAlagamentoResponse(Long id, Long microRegiaoId, String microRegiaoNome,
        LocalDate dataOcorrencia, Integer gravidade, Integer duracaoMinutos) {

    public static HistoricoAlagamentoResponse from(HistoricoAlagamento h) {
        return new HistoricoAlagamentoResponse(h.getId(), h.getMicroRegiao().getId(),
                h.getMicroRegiao().getNome(), h.getDataOcorrencia(), h.getGravidade(), h.getDuracaoMinutos());
    }
}
