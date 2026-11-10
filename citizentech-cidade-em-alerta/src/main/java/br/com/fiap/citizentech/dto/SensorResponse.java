package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.Sensor;
import br.com.fiap.citizentech.model.enums.StatusSensor;
import br.com.fiap.citizentech.model.enums.TipoSensor;

public record SensorResponse(Long id, String codigo, TipoSensor tipo, StatusSensor status,
        Long microRegiaoId, String microRegiaoNome) {

    public static SensorResponse from(Sensor s) {
        return new SensorResponse(s.getId(), s.getCodigo(), s.getTipo(), s.getStatus(),
                s.getMicroRegiao().getId(), s.getMicroRegiao().getNome());
    }
}
