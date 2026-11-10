package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.RegiaoDAO;
import br.com.fiap.citizentech.dto.RegiaoRequest;
import br.com.fiap.citizentech.dto.RegiaoResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.Regiao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegiaoBO {

    private final RegiaoDAO regiaoDAO;
    private final MicroRegiaoDAO microRegiaoDAO;

    public RegiaoBO(RegiaoDAO regiaoDAO, MicroRegiaoDAO microRegiaoDAO) {
        this.regiaoDAO = regiaoDAO;
        this.microRegiaoDAO = microRegiaoDAO;
    }

    @Transactional(readOnly = true)
    public List<RegiaoResponse> listar() {
        return regiaoDAO.findAll().stream().map(RegiaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RegiaoResponse buscarPorId(Long id) {
        return RegiaoResponse.from(obter(id));
    }

    @Transactional
    public RegiaoResponse criar(RegiaoRequest req) {
        Regiao regiao = new Regiao();
        preencher(regiao, req);
        return RegiaoResponse.from(regiaoDAO.save(regiao));
    }

    @Transactional
    public RegiaoResponse atualizar(Long id, RegiaoRequest req) {
        Regiao regiao = obter(id);
        preencher(regiao, req);
        return RegiaoResponse.from(regiaoDAO.save(regiao));
    }

    @Transactional
    public void excluir(Long id) {
        Regiao regiao = obter(id);
        if (microRegiaoDAO.existsByRegiaoId(id)) {
            throw new RegraNegocioException("Não é possível excluir a região: ela possui microrregiões cadastradas.");
        }
        regiaoDAO.delete(regiao);
    }

    private void preencher(Regiao regiao, RegiaoRequest req) {
        regiao.setNome(req.nome().trim());
        regiao.setCidade(req.cidade().trim());
    }

    private Regiao obter(Long id) {
        return regiaoDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Região não encontrada: id " + id));
    }
}
