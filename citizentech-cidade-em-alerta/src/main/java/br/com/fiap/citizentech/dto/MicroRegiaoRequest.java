package br.com.fiap.citizentech.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MicroRegiaoRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres") String nome,
        @NotNull(message = "A latitude é obrigatória") @DecimalMin(value = "-90.0", message = "Latitude mínima: -90") @DecimalMax(value = "90.0", message = "Latitude máxima: 90") Double latitude,
        @NotNull(message = "A longitude é obrigatória") @DecimalMin(value = "-180.0", message = "Longitude mínima: -180") @DecimalMax(value = "180.0", message = "Longitude máxima: 180") Double longitude,
        @NotNull(message = "A capacidade de escoamento é obrigatória") @Min(value = 0, message = "Mínimo: 0") @Max(value = 100, message = "Máximo: 100") Integer capacidadeEscoamento,
        @NotNull(message = "A região é obrigatória") Long regiaoId) {
}
