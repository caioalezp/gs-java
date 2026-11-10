package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.enums.NivelRisco;
import br.com.fiap.citizentech.model.enums.StatusAlerta;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlertaRequest(
        @NotNull(message = "A microrregião é obrigatória") Long microRegiaoId,
        @NotNull(message = "O nível é obrigatório (BAIXO, MODERADO, ALTO ou CRITICO)") NivelRisco nivel,
        @NotNull(message = "A probabilidade é obrigatória") @DecimalMin(value = "0.0", message = "Mínimo: 0") @DecimalMax(value = "100.0", message = "Máximo: 100") Double probabilidade,
        @NotNull(message = "A confiança é obrigatória") @DecimalMin(value = "0.0", message = "Mínimo: 0") @DecimalMax(value = "100.0", message = "Máximo: 100") Double confianca,
        @NotBlank(message = "A mensagem é obrigatória") @Size(max = 400, message = "A mensagem deve ter no máximo 400 caracteres") String mensagem,
        @Size(max = 300, message = "A rota alternativa deve ter no máximo 300 caracteres") String rotaAlternativa,
        StatusAlerta status) {
}
