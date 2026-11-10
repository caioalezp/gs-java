package br.com.fiap.citizentech.dto;

import br.com.fiap.citizentech.model.Alerta;
import br.com.fiap.citizentech.model.EquipeAtendimento;
import br.com.fiap.citizentech.model.enums.NivelRisco;
import br.com.fiap.citizentech.model.enums.StatusAlerta;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record AlertaResponse(Long id, Long microRegiaoId, String microRegiaoNome, NivelRisco nivel,
        Double probabilidade, Double confianca, String mensagem, String rotaAlternativa,
        LocalDateTime dataGeracao, StatusAlerta status, List<EquipeAtendimentoResponse> equipes) {

    public static AlertaResponse from(Alerta a) {
        List<EquipeAtendimentoResponse> equipes = a.getEquipes().stream()
                .sorted(Comparator.comparing(EquipeAtendimento::getId))
                .map(EquipeAtendimentoResponse::from)
                .toList();
        return new AlertaResponse(a.getId(), a.getMicroRegiao().getId(), a.getMicroRegiao().getNome(),
                a.getNivel(), a.getProbabilidade(), a.getConfianca(), a.getMensagem(), a.getRotaAlternativa(),
                a.getDataGeracao(), a.getStatus(), equipes);
    }
}
