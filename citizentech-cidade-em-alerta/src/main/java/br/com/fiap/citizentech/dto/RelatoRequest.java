package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.enums.StatusRelato;
import br.com.fiap.citizentech.model.enums.TipoRelato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RelatoRequest(
        @NotNull(message = "O morador é obrigatório") Long moradorId,
        @NotNull(message = "A microrregião é obrigatória") Long microRegiaoId,
        @NotNull(message = "O tipo é obrigatório") TipoRelato tipo,
        @NotBlank(message = "A descrição é obrigatória") @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres") String descricao,
        StatusRelato status) {
}
