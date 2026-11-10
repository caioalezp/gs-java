package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.MoradorDAO;
import br.com.fiap.citizentech.dao.RelatoDAO;
import br.com.fiap.citizentech.dto.RelatoRequest;
import br.com.fiap.citizentech.dto.RelatoResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.model.MicroRegiao;
import br.com.fiap.citizentech.model.Morador;
import br.com.fiap.citizentech.model.Relato;
import br.com.fiap.citizentech.model.enums.StatusRelato;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelatoBO {

    private final RelatoDAO relatoDAO;
    private final MoradorDAO moradorDAO;
    private final MicroRegiaoDAO microRegiaoDAO;

    public RelatoBO(RelatoDAO relatoDAO, MoradorDAO moradorDAO, MicroRegiaoDAO microRegiaoDAO) {
        this.relatoDAO = relatoDAO;
        this.moradorDAO = moradorDAO;
        this.microRegiaoDAO = microRegiaoDAO;
    }

    @Transactional(readOnly = true)
    public List<RelatoResponse> listar(Long microRegiaoId) {
        List<Relato> lista = microRegiaoId == null ? relatoDAO.findAll() : relatoDAO.buscarPorMicroRegiao(microRegiaoId);
        return lista.stream().map(RelatoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RelatoResponse buscarPorId(Long id) {
        return RelatoResponse.from(obter(id));
    }

    @Transactional
    public RelatoResponse criar(RelatoRequest req) {
        Relato relato = new Relato();
        relato.setDataHora(LocalDateTime.now());
        relato.setStatus(StatusRelato.PENDENTE);
        preencher(relato, req);
        return RelatoResponse.from(relatoDAO.save(relato));
    }

    @Transactional
    public RelatoResponse atualizar(Long id, RelatoRequest req) {
        Relato relato = obter(id);
        preencher(relato, req);
        return RelatoResponse.from(relatoDAO.save(relato));
    }

    @Transactional
    public void excluir(Long id) {
        relatoDAO.delete(obter(id));
    }

    private void preencher(Relato relato, RelatoRequest req) {
        Morador morador = moradorDAO.findById(req.moradorId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Morador não encontrado: id " + req.moradorId()));
        MicroRegiao micro = microRegiaoDAO.findById(req.microRegiaoId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + req.microRegiaoId()));
        relato.setMorador(morador);
        relato.setMicroRegiao(micro);
        relato.setTipo(req.tipo());
        relato.setDescricao(req.descricao().trim());
        if (req.status() != null) {
            relato.setStatus(req.status());
        }
    }

    private Relato obter(Long id) {
        return relatoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relato não encontrado: id " + id));
    }
}
