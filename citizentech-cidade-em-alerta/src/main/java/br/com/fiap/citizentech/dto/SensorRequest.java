package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.enums.StatusSensor;
import br.com.fiap.citizentech.model.enums.TipoSensor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SensorRequest(
        @NotBlank(message = "O código é obrigatório") @Size(max = 50, message = "O código deve ter no máximo 50 caracteres") String codigo,
        @NotNull(message = "O tipo é obrigatório (CHUVA, NIVEL_AGUA ou VIA)") TipoSensor tipo,
        @NotNull(message = "O status é obrigatório (ATIVO, INATIVO ou MANUTENCAO)") StatusSensor status,
        @NotNull(message = "A microrregião é obrigatória") Long microRegiaoId) {
}
