package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipeAtendimentoRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres") String nome,
        @NotBlank(message = "A especialidade é obrigatória") @Size(max = 100, message = "A especialidade deve ter no máximo 100 caracteres") String especialidade,
        DisponibilidadeEquipe disponibilidade) {
}
