package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.AlertaDAO;
import br.com.fiap.citizentech.dao.EquipeAtendimentoDAO;
import br.com.fiap.citizentech.dao.HistoricoAlagamentoDAO;
import br.com.fiap.citizentech.dao.LeituraSensorDAO;
import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.RelatoDAO;
import br.com.fiap.citizentech.dto.AlertaRequest;
import br.com.fiap.citizentech.dto.AlertaResponse;
import br.com.fiap.citizentech.dto.PrioridadeResponse;
import br.com.fiap.citizentech.exception.ConflitoException;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.Alerta;
import br.com.fiap.citizentech.model.EquipeAtendimento;
import br.com.fiap.citizentech.model.HistoricoAlagamento;
import br.com.fiap.citizentech.model.LeituraSensor;
import br.com.fiap.citizentech.model.MicroRegiao;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;
import br.com.fiap.citizentech.model.enums.NivelRisco;
import br.com.fiap.citizentech.model.enums.StatusAlerta;
import br.com.fiap.citizentech.model.enums.StatusRelato;
import br.com.fiap.citizentech.model.enums.StatusSensor;
import br.com.fiap.citizentech.model.enums.TipoSensor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regras de negócio dos alertas, incluindo o Alerta Antecipado por Microrregião. */
@Service
public class AlertaBO {

    private static final int JANELA_SENSOR_HORAS = 6;
    private static final int JANELA_RELATOS_HORAS = 6;
    private static final int JANELA_HISTORICO_ANOS = 5;
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final AlertaDAO alertaDAO;
    private final MicroRegiaoDAO microRegiaoDAO;
    private final LeituraSensorDAO leituraDAO;
    private final RelatoDAO relatoDAO;
    private final HistoricoAlagamentoDAO historicoDAO;
    private final EquipeAtendimentoDAO equipeDAO;

    public AlertaBO(AlertaDAO alertaDAO, MicroRegiaoDAO microRegiaoDAO, LeituraSensorDAO leituraDAO,
            RelatoDAO relatoDAO, HistoricoAlagamentoDAO historicoDAO, EquipeAtendimentoDAO equipeDAO) {
        this.alertaDAO = alertaDAO;
        this.microRegiaoDAO = microRegiaoDAO;
        this.leituraDAO = leituraDAO;
        this.relatoDAO = relatoDAO;
        this.historicoDAO = historicoDAO;
        this.equipeDAO = equipeDAO;
    }

    // ------------------------------------------------------------------ CRUD

    @Transactional(readOnly = true)
    public List<AlertaResponse> listar(Long microRegiaoId) {
        List<Alerta> lista = microRegiaoId == null ? alertaDAO.findAllByOrderByDataGeracaoDesc()
                : alertaDAO.findByMicroRegiaoIdOrderByDataGeracaoDesc(microRegiaoId);
        return lista.stream().map(AlertaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AlertaResponse buscarPorId(Long id) {
        return AlertaResponse.from(obter(id));
    }

    @Transactional
    public AlertaResponse criar(AlertaRequest req) {
        if (req.status() == StatusAlerta.EM_ATENDIMENTO) {
            throw new RegraNegocioException("Um alerta só entra EM_ATENDIMENTO após a designação de uma equipe.");
        }
        Alerta alerta = new Alerta();
        alerta.setDataGeracao(LocalDateTime.now());
        alerta.setStatus(req.status() != null ? req.status() : StatusAlerta.ATIVO);
        preencher(alerta, req);
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    @Transactional
    public AlertaResponse atualizar(Long id, AlertaRequest req) {
        Alerta alerta = obter(id);
        StatusAlerta novoStatus = req.status() != null ? req.status() : alerta.getStatus();
        if (novoStatus == StatusAlerta.EM_ATENDIMENTO && alerta.getEquipes().isEmpty()) {
            throw new RegraNegocioException("Designe ao menos uma equipe antes de colocar o alerta EM_ATENDIMENTO.");
        }
        preencher(alerta, req);
        alerta.setStatus(novoStatus);
        if (novoStatus == StatusAlerta.ENCERRADO) {
            liberarTodas(alerta);
        }
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    @Transactional
    public void excluir(Long id) {
        Alerta alerta = obter(id);
        liberarTodas(alerta);
        alertaDAO.delete(alerta);
    }

    // ------------------------------------------------------ Alerta Antecipado

    /** Calcula o risco da microrregião e persiste o alerta gerado. */
    @Transactional
    public AlertaResponse calcular(Long microRegiaoId) {
        MicroRegiao micro = obterMicro(microRegiaoId);
        LocalDateTime agora = LocalDateTime.now();
        CalculadoraRisco.Resultado r = avaliar(micro, agora);

        Alerta alerta = new Alerta();
        alerta.setMicroRegiao(micro);
        alerta.setNivel(r.nivel());
        alerta.setProbabilidade(r.probabilidade());
        alerta.setConfianca(r.confianca());
        alerta.setDataGeracao(agora);
        alerta.setStatus(r.nivel() == NivelRisco.BAIXO ? StatusAlerta.ENCERRADO : StatusAlerta.ATIVO);
        alerta.setMensagem(montarMensagem(micro, r));
        alerta.setRotaAlternativa(sugerirRota(micro));
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    /** Ranking das microrregiões que precisam de atenção primeiro (maior pontuação = maior prioridade). */
    @Transactional(readOnly = true)
    public List<PrioridadeResponse> prioridade() {
        LocalDateTime agora = LocalDateTime.now();
        return microRegiaoDAO.findAll().stream()
                .map(m -> {
                    CalculadoraRisco.Resultado r = avaliar(m, agora);
                    return new PrioridadeResponse(m.getId(), m.getNome(), m.getRegiao().getNome(),
                            r.probabilidade(), r.confianca(), r.nivel(),
                            CalculadoraRisco.pontuacaoPrioridade(r.probabilidade(), r.confianca()));
                })
                .sorted(Comparator.comparing(PrioridadeResponse::pontuacao).reversed())
                .toList();
    }

    // ------------------------------------------------------------- Equipes

    @Transactional
    public AlertaResponse designarEquipe(Long alertaId, Long equipeId) {
        Alerta alerta = obter(alertaId);
        EquipeAtendimento equipe = obterEquipe(equipeId);
        if (alerta.getStatus() == StatusAlerta.ENCERRADO) {
            throw new RegraNegocioException("Não é possível designar equipe para um alerta ENCERRADO.");
        }
        if (alerta.getEquipes().contains(equipe)) {
            throw new ConflitoException("A equipe já está designada para este alerta.");
        }
        if (equipe.getDisponibilidade() == DisponibilidadeEquipe.OCUPADA) {
            throw new RegraNegocioException("A equipe " + equipe.getNome() + " está OCUPADA em outro atendimento.");
        }
        alerta.getEquipes().add(equipe);
        alerta.setStatus(StatusAlerta.EM_ATENDIMENTO);
        equipe.setDisponibilidade(DisponibilidadeEquipe.OCUPADA);
        equipeDAO.save(equipe);
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    @Transactional
    public AlertaResponse liberarEquipe(Long alertaId, Long equipeId) {
        Alerta alerta = obter(alertaId);
        EquipeAtendimento equipe = obterEquipe(equipeId);
        if (!alerta.getEquipes().remove(equipe)) {
            throw new RecursoNaoEncontradoException("A equipe " + equipeId + " não está designada ao alerta " + alertaId);
        }
        equipe.setDisponibilidade(DisponibilidadeEquipe.DISPONIVEL);
        equipeDAO.save(equipe);
        if (alerta.getEquipes().isEmpty() && alerta.getStatus() == StatusAlerta.EM_ATENDIMENTO) {
            alerta.setStatus(StatusAlerta.ATIVO);
        }
        return AlertaResponse.from(alertaDAO.save(alerta));
    }

    // ------------------------------------------------------------ Internos

    private CalculadoraRisco.Resultado avaliar(MicroRegiao micro, LocalDateTime agora) {
        LocalDateTime desde = agora.minusHours(JANELA_SENSOR_HORAS);
        List<LeituraSensor> chuva = ativas(leituraDAO.buscarRecentes(micro.getId(), TipoSensor.CHUVA, desde));
        List<LeituraSensor> agua = ativas(leituraDAO.buscarRecentes(micro.getId(), TipoSensor.NIVEL_AGUA, desde));

        LocalDateTime ultimaLeitura = Stream.concat(chuva.stream(), agua.stream())
                .map(LeituraSensor::getDataHora)
                .max(Comparator.naturalOrder())
                .orElse(null);

        LocalDate limite = agora.toLocalDate().minusYears(JANELA_HISTORICO_ANOS);
        List<HistoricoAlagamento> historico = historicoDAO.findByMicroRegiaoId(micro.getId()).stream()
                .filter(h -> !h.getDataOcorrencia().isBefore(limite))
                .toList();
        double somaGravidade = historico.stream().mapToInt(HistoricoAlagamento::getGravidade).sum();

        long relatos = relatoDAO.contarRecentes(micro.getId(), StatusRelato.DESCARTADO,
                agora.minusHours(JANELA_RELATOS_HORAS));

        CalculadoraRisco.Entrada entrada = new CalculadoraRisco.Entrada(media(chuva), media(agua), ultimaLeitura,
                somaGravidade, historico.size(), micro.getCapacidadeEscoamento(), relatos, agora);
        return CalculadoraRisco.calcular(entrada);
    }

    private List<LeituraSensor> ativas(List<LeituraSensor> leituras) {
        return leituras.stream().filter(l -> l.getSensor().getStatus() == StatusSensor.ATIVO).toList();
    }

    private Double media(List<LeituraSensor> leituras) {
        if (leituras.isEmpty()) {
            return null;
        }
        return leituras.stream().mapToDouble(LeituraSensor::getValor).average().orElse(0.0);
    }

    private String montarMensagem(MicroRegiao micro, CalculadoraRisco.Resultado r) {
        String texto = String.format(PT_BR,
                "Risco %s de alagamento em %s: probabilidade de %.1f%% e confiança de %.1f%%.",
                r.nivel(), micro.getNome(), r.probabilidade(), r.confianca());
        if (r.confianca() < 50.0) {
            texto += " Previsão de baixa confiança: monitore e aguarde confirmação antes de agir.";
        }
        return texto;
    }

    private String sugerirRota(MicroRegiao origem) {
        record Candidata(MicroRegiao micro, double km) {
        }
        return microRegiaoDAO.findByRegiaoId(origem.getRegiao().getId()).stream()
                .filter(c -> !c.getId().equals(origem.getId()))
                .filter(c -> !emRisco(c.getId()))
                .map(c -> new Candidata(c, CalculadoraRisco.distanciaKm(origem.getLatitude(),
                        origem.getLongitude(), c.getLatitude(), c.getLongitude())))
                .min(Comparator.comparingDouble(Candidata::km))
                .map(c -> String.format(PT_BR, "Desviar por %s (a %.1f km do ponto de risco).",
                        c.micro().getNome(), c.km()))
                .orElse("Nenhuma rota alternativa segura cadastrada para esta região.");
    }

    private boolean emRisco(Long microRegiaoId) {
        return alertaDAO.findFirstByMicroRegiaoIdOrderByDataGeracaoDesc(microRegiaoId)
                .map(a -> a.getStatus() != StatusAlerta.ENCERRADO
                        && (a.getNivel() == NivelRisco.ALTO || a.getNivel() == NivelRisco.CRITICO))
                .orElse(false);
    }

    private void liberarTodas(Alerta alerta) {
        for (EquipeAtendimento equipe : alerta.getEquipes()) {
            equipe.setDisponibilidade(DisponibilidadeEquipe.DISPONIVEL);
            equipeDAO.save(equipe);
        }
    }

    private void preencher(Alerta alerta, AlertaRequest req) {
        alerta.setMicroRegiao(obterMicro(req.microRegiaoId()));
        alerta.setNivel(req.nivel());
        alerta.setProbabilidade(req.probabilidade());
        alerta.setConfianca(req.confianca());
        alerta.setMensagem(req.mensagem().trim());
        alerta.setRotaAlternativa(req.rotaAlternativa() == null || req.rotaAlternativa().isBlank()
                ? null : req.rotaAlternativa().trim());
    }

    private Alerta obter(Long id) {
        return alertaDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta não encontrado: id " + id));
    }

    private MicroRegiao obterMicro(Long id) {
        return microRegiaoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + id));
    }

    private EquipeAtendimento obterEquipe(Long id) {
        return equipeDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe não encontrada: id " + id));
    }
}
