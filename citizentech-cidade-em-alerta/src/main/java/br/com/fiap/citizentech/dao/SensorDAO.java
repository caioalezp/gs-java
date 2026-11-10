package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.Sensor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDAO extends JpaRepository<Sensor, Long> {

    List<Sensor> findByMicroRegiaoId(Long microRegiaoId);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    boolean existsByMicroRegiaoId(Long microRegiaoId);
}
