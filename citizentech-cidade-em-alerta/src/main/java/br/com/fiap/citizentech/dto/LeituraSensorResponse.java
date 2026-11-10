package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.LeituraSensor;
import java.time.LocalDateTime;

public record LeituraSensorResponse(Long id, Long sensorId, String sensorCodigo, Double valor,
        String unidade, LocalDateTime dataHora) {

    public static LeituraSensorResponse from(LeituraSensor l) {
        return new LeituraSensorResponse(l.getId(), l.getSensor().getId(), l.getSensor().getCodigo(),
                l.getValor(), l.getUnidade(), l.getDataHora());
    }
}
