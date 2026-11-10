package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.enums.NivelRisco;

/** Item do ranking de microrregiões que precisam de atenção primeiro. */
public record PrioridadeResponse(Long microRegiaoId, String microRegiao, String regiao,
        Double probabilidade, Double confianca, NivelRisco nivel, Double pontuacao) {
}
