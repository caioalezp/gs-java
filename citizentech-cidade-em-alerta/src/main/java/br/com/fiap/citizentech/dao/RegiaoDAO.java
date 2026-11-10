package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegiaoDAO extends JpaRepository<Regiao, Long> {
}
