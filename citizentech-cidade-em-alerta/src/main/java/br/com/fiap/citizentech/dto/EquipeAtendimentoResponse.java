package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.EquipeAtendimento;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;

public record EquipeAtendimentoResponse(Long id, String nome, String especialidade,
        DisponibilidadeEquipe disponibilidade) {

    public static EquipeAtendimentoResponse from(EquipeAtendimento e) {
        return new EquipeAtendimentoResponse(e.getId(), e.getNome(), e.getEspecialidade(), e.getDisponibilidade());
    }
}
