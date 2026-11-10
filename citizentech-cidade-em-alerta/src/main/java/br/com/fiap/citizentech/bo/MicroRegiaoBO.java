package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.AlertaDAO;
import br.com.fiap.citizentech.dao.HistoricoAlagamentoDAO;
import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.MoradorDAO;
import br.com.fiap.citizentech.dao.RegiaoDAO;
import br.com.fiap.citizentech.dao.RelatoDAO;
import br.com.fiap.citizentech.dao.SensorDAO;
import br.com.fiap.citizentech.dto.MicroRegiaoRequest;
import br.com.fiap.citizentech.dto.MicroRegiaoResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.MicroRegiao;
import br.com.fiap.citizentech.model.Regiao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MicroRegiaoBO {

    private final MicroRegiaoDAO microRegiaoDAO;
    private final RegiaoDAO regiaoDAO;
    private final SensorDAO sensorDAO;
    private final MoradorDAO moradorDAO;
    private final RelatoDAO relatoDAO;
    private final HistoricoAlagamentoDAO historicoDAO;
    private final AlertaDAO alertaDAO;

    public MicroRegiaoBO(MicroRegiaoDAO microRegiaoDAO, RegiaoDAO regiaoDAO, SensorDAO sensorDAO,
            MoradorDAO moradorDAO, RelatoDAO relatoDAO, HistoricoAlagamentoDAO historicoDAO, AlertaDAO alertaDAO) {
        this.microRegiaoDAO = microRegiaoDAO;
        this.regiaoDAO = regiaoDAO;
        this.sensorDAO = sensorDAO;
        this.moradorDAO = moradorDAO;
        this.relatoDAO = relatoDAO;
        this.historicoDAO = historicoDAO;
        this.alertaDAO = alertaDAO;
    }

    @Transactional(readOnly = true)
    public List<MicroRegiaoResponse> listar(Long regiaoId) {
        List<MicroRegiao> lista = regiaoId == null ? microRegiaoDAO.findAll() : microRegiaoDAO.findByRegiaoId(regiaoId);
        return lista.stream().map(MicroRegiaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MicroRegiaoResponse buscarPorId(Long id) {
        return MicroRegiaoResponse.from(obter(id));
    }

    @Transactional
    public MicroRegiaoResponse criar(MicroRegiaoRequest req) {
        MicroRegiao micro = new MicroRegiao();
        preencher(micro, req);
        return MicroRegiaoResponse.from(microRegiaoDAO.save(micro));
    }

    @Transactional
    public MicroRegiaoResponse atualizar(Long id, MicroRegiaoRequest req) {
        MicroRegiao micro = obter(id);
        preencher(micro, req);
        return MicroRegiaoResponse.from(microRegiaoDAO.save(micro));
    }

    @Transactional
    public void excluir(Long id) {
        MicroRegiao micro = obter(id);
        if (sensorDAO.existsByMicroRegiaoId(id) || moradorDAO.existsByMicroRegiaoId(id)
                || relatoDAO.existsByMicroRegiaoId(id) || historicoDAO.existsByMicroRegiaoId(id)
                || alertaDAO.existsByMicroRegiaoId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir a microrregião: há sensores, moradores, relatos, histórico ou alertas vinculados.");
        }
        microRegiaoDAO.delete(micro);
    }

    private void preencher(MicroRegiao micro, MicroRegiaoRequest req) {
        Regiao regiao = regiaoDAO.findById(req.regiaoId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Região não encontrada: id " + req.regiaoId()));
        micro.setNome(req.nome().trim());
        micro.setLatitude(req.latitude());
        micro.setLongitude(req.longitude());
        micro.setCapacidadeEscoamento(req.capacidadeEscoamento());
        micro.setRegiao(regiao);
    }

    private MicroRegiao obter(Long id) {
        return microRegiaoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + id));
    }
}
