package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.Morador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoradorDAO extends JpaRepository<Morador, Long> {

    List<Morador> findByMicroRegiaoId(Long microRegiaoId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByMicroRegiaoId(Long microRegiaoId);
}
