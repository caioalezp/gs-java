package br.com.fiap.citizentech.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record HistoricoAlagamentoRequest(
        @NotNull(message = "A microrregião é obrigatória") Long microRegiaoId,
        @NotNull(message = "A data da ocorrência é obrigatória") @PastOrPresent(message = "A data não pode estar no futuro") LocalDate dataOcorrencia,
        @NotNull(message = "A gravidade é obrigatória") @Min(value = 1, message = "Gravidade mínima: 1") @Max(value = 5, message = "Gravidade máxima: 5") Integer gravidade,
        @NotNull(message = "A duração em minutos é obrigatória") @Min(value = 1, message = "A duração deve ser de pelo menos 1 minuto") Integer duracaoMinutos) {
}
