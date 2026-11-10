package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.MicroRegiao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicroRegiaoDAO extends JpaRepository<MicroRegiao, Long> {

    List<MicroRegiao> findByRegiaoId(Long regiaoId);

    boolean existsByRegiaoId(Long regiaoId);
}
