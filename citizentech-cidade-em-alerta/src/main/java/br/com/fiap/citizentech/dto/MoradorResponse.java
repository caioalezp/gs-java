package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.Morador;

public record MoradorResponse(Long id, String nome, String email, String telefone,
        Long microRegiaoId, String microRegiaoNome) {

    public static MoradorResponse from(Morador m) {
        return new MoradorResponse(m.getId(), m.getNome(), m.getEmail(), m.getTelefone(),
                m.getMicroRegiao().getId(), m.getMicroRegiao().getNome());
    }
}
