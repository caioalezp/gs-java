package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.MoradorDAO;
import br.com.fiap.citizentech.dao.RelatoDAO;
import br.com.fiap.citizentech.dto.MoradorRequest;
import br.com.fiap.citizentech.dto.MoradorResponse;
import br.com.fiap.citizentech.exception.ConflitoException;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.MicroRegiao;
import br.com.fiap.citizentech.model.Morador;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoradorBO {

    private final MoradorDAO moradorDAO;
    private final MicroRegiaoDAO microRegiaoDAO;
    private final RelatoDAO relatoDAO;

    public MoradorBO(MoradorDAO moradorDAO, MicroRegiaoDAO microRegiaoDAO, RelatoDAO relatoDAO) {
        this.moradorDAO = moradorDAO;
        this.microRegiaoDAO = microRegiaoDAO;
        this.relatoDAO = relatoDAO;
    }

    @Transactional(readOnly = true)
    public List<MoradorResponse> listar(Long microRegiaoId) {
        List<Morador> lista = microRegiaoId == null ? moradorDAO.findAll() : moradorDAO.findByMicroRegiaoId(microRegiaoId);
        return lista.stream().map(MoradorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MoradorResponse buscarPorId(Long id) {
        return MoradorResponse.from(obter(id));
    }

    @Transactional
    public MoradorResponse criar(MoradorRequest req) {
        String email = req.email().trim().toLowerCase();
        if (moradorDAO.existsByEmail(email)) {
            throw new ConflitoException("Já existe um morador cadastrado com o e-mail " + email);
        }
        Morador morador = new Morador();
        preencher(morador, req);
        return MoradorResponse.from(moradorDAO.save(morador));
    }

    @Transactional
    public MoradorResponse atualizar(Long id, MoradorRequest req) {
        Morador morador = obter(id);
        String email = req.email().trim().toLowerCase();
        if (moradorDAO.existsByEmailAndIdNot(email, id)) {
            throw new ConflitoException("Já existe outro morador cadastrado com o e-mail " + email);
        }
        preencher(morador, req);
        return MoradorResponse.from(moradorDAO.save(morador));
    }

    @Transactional
    public void excluir(Long id) {
        Morador morador = obter(id);
        if (relatoDAO.existsByMoradorId(id)) {
            throw new RegraNegocioException("Não é possível excluir o morador: ele possui relatos registrados.");
        }
        moradorDAO.delete(morador);
    }

    private void preencher(Morador morador, MoradorRequest req) {
        MicroRegiao micro = microRegiaoDAO.findById(req.microRegiaoId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + req.microRegiaoId()));
        morador.setNome(req.nome().trim());
        morador.setEmail(req.email().trim().toLowerCase());
        morador.setTelefone(req.telefone() == null || req.telefone().isBlank() ? null : req.telefone().trim());
        morador.setMicroRegiao(micro);
    }

    private Morador obter(Long id) {
        return moradorDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Morador não encontrado: id " + id));
    }
}
