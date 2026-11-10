package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.model.enums.NivelRisco;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Regra central do Alerta Antecipado por Microrregião.
 *
 * Probabilidade (0-100) = 30% chuva + 20% histórico + 20% (100 - escoamento) + 20% nível da água + 10% relatos.
 * Confiança (0-100) cresce com o número de fontes independentes que confirmam o risco
 * (sensor, relatos, histórico) e com a atualidade da última leitura de sensor.
 */
public final class CalculadoraRisco {

    public static final double PESO_CHUVA = 0.30;
    public static final double PESO_HISTORICO = 0.20;
    public static final double PESO_ESCOAMENTO = 0.20;
    public static final double PESO_NIVEL_AGUA = 0.20;
    public static final double PESO_RELATOS = 0.10;

    private CalculadoraRisco() {
    }

    /**
     * Dados já coletados para a microrregião.
     *
     * @param chuvaMmH média da chuva em mm/h na janela recente (null se não há leitura)
     * @param nivelAguaCm média do nível da água em cm (null se não há leitura)
     * @param ultimaLeitura data/hora da leitura de sensor mais recente (null se não há)
     * @param somaGravidadeHistorico soma das gravidades (1-5) dos alagamentos dos últimos anos
     * @param qtdHistorico quantidade de alagamentos registrados no período
     * @param capacidadeEscoamento capacidade de escoamento da microrregião (0-100)
     * @param relatosRecentes quantidade de relatos recentes não descartados
     * @param agora instante de referência do cálculo
     */
    public record Entrada(Double chuvaMmH, Double nivelAguaCm, LocalDateTime ultimaLeitura,
            double somaGravidadeHistorico, int qtdHistorico, int capacidadeEscoamento,
            long relatosRecentes, LocalDateTime agora) {
    }

    public record Resultado(double probabilidade, double confianca, NivelRisco nivel) {
    }

    public static Resultado calcular(Entrada e) {
        double chuva = e.chuvaMmH() == null ? 0.0 : limitar(e.chuvaMmH() * 2.0);
        double agua = e.nivelAguaCm() == null ? 0.0 : limitar(e.nivelAguaCm());
        double historico = limitar(e.somaGravidadeHistorico() * 5.0);
        double escoamento = 100.0 - limitar(e.capacidadeEscoamento());
        double relatos = limitar(e.relatosRecentes() * 25.0);

        double probabilidade = PESO_CHUVA * chuva + PESO_HISTORICO * historico
                + PESO_ESCOAMENTO * escoamento + PESO_NIVEL_AGUA * agua + PESO_RELATOS * relatos;
        probabilidade = arredondar(limitar(probabilidade));

        return new Resultado(probabilidade, calcularConfianca(e), NivelRisco.fromProbabilidade(probabilidade));
    }

    static double calcularConfianca(Entrada e) {
        boolean temSensor = e.chuvaMmH() != null || e.nivelAguaCm() != null;
        double confianca = 25.0;
        if (temSensor) {
            confianca += 25.0;
        }
        if (e.relatosRecentes() > 0) {
            confianca += 20.0;
        }
        if (e.qtdHistorico() > 0) {
            confianca += 15.0;
        }
        confianca += 15.0 * atualidade(e.ultimaLeitura(), e.agora());
        return arredondar(limitar(confianca));
    }

    /** 1,0 até 30 minutos; decai linearmente até 0 em 6 horas. */
    static double atualidade(LocalDateTime ultimaLeitura, LocalDateTime agora) {
        if (ultimaLeitura == null) {
            return 0.0;
        }
        long minutos = Math.max(0, Duration.between(ultimaLeitura, agora).toMinutes());
        if (minutos <= 30) {
            return 1.0;
        }
        return Math.max(0.0, 1.0 - (minutos - 30) / 330.0);
    }

    /** Pontuação para ordenar a prioridade de atendimento: risco ponderado pela confiança. */
    public static double pontuacaoPrioridade(double probabilidade, double confianca) {
        return arredondar(probabilidade * (0.5 + confianca / 200.0));
    }

    /** Distância em km entre dois pontos (fórmula de Haversine). */
    public static double distanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double raioTerra = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return raioTerra * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private static double limitar(double v) {
        return Math.max(0.0, Math.min(100.0, v));
    }

    private static double arredondar(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
