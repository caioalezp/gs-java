package br.com.fiap.citizentech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MoradorRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres") String nome,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres") String email,
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres") String telefone,
        @NotNull(message = "A microrregião de interesse é obrigatória") Long microRegiaoId) {
}
