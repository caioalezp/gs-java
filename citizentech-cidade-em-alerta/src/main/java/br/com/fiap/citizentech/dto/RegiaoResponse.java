package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.Regiao;

public record RegiaoResponse(Long id, String nome, String cidade) {

    public static RegiaoResponse from(Regiao r) {
        return new RegiaoResponse(r.getId(), r.getNome(), r.getCidade());
    }
}
