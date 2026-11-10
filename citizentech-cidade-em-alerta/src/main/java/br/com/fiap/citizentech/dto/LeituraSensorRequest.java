package br.com.fiap.citizentech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record LeituraSensorRequest(
        @NotNull(message = "O sensor é obrigatório") Long sensorId,
        @NotNull(message = "O valor é obrigatório") @DecimalMin(value = "0.0", message = "O valor não pode ser negativo") Double valor,
        @NotBlank(message = "A unidade é obrigatória") @Size(max = 20, message = "A unidade deve ter no máximo 20 caracteres") String unidade,
        @PastOrPresent(message = "A data/hora não pode estar no futuro") LocalDateTime dataHora) {
}
