package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.EquipeAtendimentoDAO;
import br.com.fiap.citizentech.dto.EquipeAtendimentoRequest;
import br.com.fiap.citizentech.dto.EquipeAtendimentoResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.EquipeAtendimento;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipeAtendimentoBO {

    private final EquipeAtendimentoDAO equipeDAO;

    public EquipeAtendimentoBO(EquipeAtendimentoDAO equipeDAO) {
        this.equipeDAO = equipeDAO;
    }

    @Transactional(readOnly = true)
    public List<EquipeAtendimentoResponse> listar(DisponibilidadeEquipe disponibilidade) {
        List<EquipeAtendimento> lista = disponibilidade == null ? equipeDAO.findAll()
                : equipeDAO.findByDisponibilidade(disponibilidade);
        return lista.stream().map(EquipeAtendimentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EquipeAtendimentoResponse buscarPorId(Long id) {
        return EquipeAtendimentoResponse.from(obter(id));
    }

    @Transactional
    public EquipeAtendimentoResponse criar(EquipeAtendimentoRequest req) {
        EquipeAtendimento equipe = new EquipeAtendimento();
        equipe.setDisponibilidade(DisponibilidadeEquipe.DISPONIVEL);
        preencher(equipe, req);
        return EquipeAtendimentoResponse.from(equipeDAO.save(equipe));
    }

    @Transactional
    public EquipeAtendimentoResponse atualizar(Long id, EquipeAtendimentoRequest req) {
        EquipeAtendimento equipe = obter(id);
        preencher(equipe, req);
        return EquipeAtendimentoResponse.from(equipeDAO.save(equipe));
    }

    @Transactional
    public void excluir(Long id) {
        EquipeAtendimento equipe = obter(id);
        if (equipe.getDisponibilidade() == DisponibilidadeEquipe.OCUPADA) {
            throw new RegraNegocioException("Não é possível excluir uma equipe que está em atendimento.");
        }
        equipeDAO.delete(equipe);
        equipeDAO.flush();
    }

    private void preencher(EquipeAtendimento equipe, EquipeAtendimentoRequest req) {
        equipe.setNome(req.nome().trim());
        equipe.setEspecialidade(req.especialidade().trim());
        if (req.disponibilidade() != null) {
            equipe.setDisponibilidade(req.disponibilidade());
        }
    }

    private EquipeAtendimento obter(Long id) {
        return equipeDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe não encontrada: id " + id));
    }
}
