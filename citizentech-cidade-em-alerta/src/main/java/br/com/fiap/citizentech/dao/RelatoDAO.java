package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.Relato;
import br.com.fiap.citizentech.model.enums.StatusRelato;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatoDAO extends JpaRepository<Relato, Long> {

    @Query("select r from Relato r where r.microRegiao.id = :microRegiaoId order by r.dataHora desc")
    List<Relato> buscarPorMicroRegiao(@Param("microRegiaoId") Long microRegiaoId);

    @Query("select count(r) from Relato r where r.microRegiao.id = :microRegiaoId "
            + "and r.status <> :descartado and r.dataHora >= :desde")
    long contarRecentes(@Param("microRegiaoId") Long microRegiaoId,
            @Param("descartado") StatusRelato descartado, @Param("desde") LocalDateTime desde);

    boolean existsByMicroRegiaoId(Long microRegiaoId);

    boolean existsByMoradorId(Long moradorId);
}
