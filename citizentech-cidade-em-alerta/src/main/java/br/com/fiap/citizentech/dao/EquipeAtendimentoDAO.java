package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.EquipeAtendimento;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeAtendimentoDAO extends JpaRepository<EquipeAtendimento, Long> {

    List<EquipeAtendimento> findByDisponibilidade(DisponibilidadeEquipe disponibilidade);
}
