package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.HistoricoAlagamentoDAO;
import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dto.HistoricoAlagamentoRequest;
import br.com.fiap.citizentech.dto.HistoricoAlagamentoResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.model.HistoricoAlagamento;
import br.com.fiap.citizentech.model.MicroRegiao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoricoAlagamentoBO {

    private final HistoricoAlagamentoDAO historicoDAO;
    private final MicroRegiaoDAO microRegiaoDAO;

    public HistoricoAlagamentoBO(HistoricoAlagamentoDAO historicoDAO, MicroRegiaoDAO microRegiaoDAO) {
        this.historicoDAO = historicoDAO;
        this.microRegiaoDAO = microRegiaoDAO;
    }

    @Transactional(readOnly = true)
    public List<HistoricoAlagamentoResponse> listar(Long microRegiaoId) {
        List<HistoricoAlagamento> lista = microRegiaoId == null ? historicoDAO.findAll()
                : historicoDAO.findByMicroRegiaoId(microRegiaoId);
        return lista.stream().map(HistoricoAlagamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public HistoricoAlagamentoResponse buscarPorId(Long id) {
        return HistoricoAlagamentoResponse.from(obter(id));
    }

    @Transactional
    public HistoricoAlagamentoResponse criar(HistoricoAlagamentoRequest req) {
        HistoricoAlagamento h = new HistoricoAlagamento();
        preencher(h, req);
        return HistoricoAlagamentoResponse.from(historicoDAO.save(h));
    }

    @Transactional
    public HistoricoAlagamentoResponse atualizar(Long id, HistoricoAlagamentoRequest req) {
        HistoricoAlagamento h = obter(id);
        preencher(h, req);
        return HistoricoAlagamentoResponse.from(historicoDAO.save(h));
    }

    @Transactional
    public void excluir(Long id) {
        historicoDAO.delete(obter(id));
    }

    private void preencher(HistoricoAlagamento h, HistoricoAlagamentoRequest req) {
        MicroRegiao micro = microRegiaoDAO.findById(req.microRegiaoId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + req.microRegiaoId()));
        h.setMicroRegiao(micro);
        h.setDataOcorrencia(req.dataOcorrencia());
        h.setGravidade(req.gravidade());
        h.setDuracaoMinutos(req.duracaoMinutos());
    }

    private HistoricoAlagamento obter(Long id) {
        return historicoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Histórico de alagamento não encontrado: id " + id));
    }
}
