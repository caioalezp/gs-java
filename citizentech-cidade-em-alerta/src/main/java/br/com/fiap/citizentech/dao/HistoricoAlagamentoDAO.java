package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.HistoricoAlagamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoAlagamentoDAO extends JpaRepository<HistoricoAlagamento, Long> {

    List<HistoricoAlagamento> findByMicroRegiaoId(Long microRegiaoId);

    boolean existsByMicroRegiaoId(Long microRegiaoId);
}
