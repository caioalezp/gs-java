package br.com.fiap.citizentech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegiaoRequest(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres") String nome,
        @NotBlank(message = "A cidade é obrigatória") @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres") String cidade) {
}
