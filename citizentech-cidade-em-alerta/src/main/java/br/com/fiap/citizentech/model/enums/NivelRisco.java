package br.com.fiap.citizentech.model.enums;

public enum NivelRisco {
    BAIXO,
    MODERADO,
    ALTO,
    CRITICO;

    /** Converte a probabilidade (0-100) no nível de risco do alerta. */
    public static NivelRisco fromProbabilidade(double probabilidade) {
        if (probabilidade >= 75.0) {
            return CRITICO;
        }
        if (probabilidade >= 50.0) {
            return ALTO;
        }
        if (probabilidade >= 25.0) {
            return MODERADO;
        }
        return BAIXO;
    }
}
