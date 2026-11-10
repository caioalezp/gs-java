package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.model.LeituraSensor;
import br.com.fiap.citizentech.model.enums.TipoSensor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeituraSensorDAO extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findTop200ByOrderByDataHoraDesc();

    @Query("select l from LeituraSensor l where l.sensor.id = :sensorId order by l.dataHora desc")
    List<LeituraSensor> buscarPorSensor(@Param("sensorId") Long sensorId);

    @Query("select l from LeituraSensor l where l.sensor.microRegiao.id = :microRegiaoId "
            + "and l.sensor.tipo = :tipo and l.dataHora >= :desde order by l.dataHora desc")
    List<LeituraSensor> buscarRecentes(@Param("microRegiaoId") Long microRegiaoId,
            @Param("tipo") TipoSensor tipo, @Param("desde") LocalDateTime desde);

    boolean existsBySensorId(Long sensorId);
}
