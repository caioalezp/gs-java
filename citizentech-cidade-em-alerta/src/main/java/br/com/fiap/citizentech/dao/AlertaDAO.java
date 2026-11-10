package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.Alerta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaDAO extends JpaRepository<Alerta, Long> {

    List<Alerta> findByMicroRegiaoIdOrderByDataGeracaoDesc(Long microRegiaoId);

    List<Alerta> findAllByOrderByDataGeracaoDesc();

    Optional<Alerta> findFirstByMicroRegiaoIdOrderByDataGeracaoDesc(Long microRegiaoId);

    boolean existsByMicroRegiaoId(Long microRegiaoId);
}
