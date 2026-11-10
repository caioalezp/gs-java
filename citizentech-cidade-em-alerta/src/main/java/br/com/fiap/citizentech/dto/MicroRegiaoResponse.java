package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.MicroRegiao;

public record MicroRegiaoResponse(Long id, String nome, Double latitude, Double longitude,
        Integer capacidadeEscoamento, Long regiaoId, String regiaoNome) {

    public static MicroRegiaoResponse from(MicroRegiao m) {
        return new MicroRegiaoResponse(m.getId(), m.getNome(), m.getLatitude(), m.getLongitude(),
                m.getCapacidadeEscoamento(), m.getRegiao().getId(), m.getRegiao().getNome());
    }
}
