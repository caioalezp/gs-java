package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.LeituraSensorDAO;
import br.com.fiap.citizentech.dao.SensorDAO;
import br.com.fiap.citizentech.dto.LeituraSensorRequest;
import br.com.fiap.citizentech.dto.LeituraSensorResponse;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.LeituraSensor;
import br.com.fiap.citizentech.model.Sensor;
import br.com.fiap.citizentech.model.enums.StatusSensor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeituraSensorBO {

    private final LeituraSensorDAO leituraDAO;
    private final SensorDAO sensorDAO;

    public LeituraSensorBO(LeituraSensorDAO leituraDAO, SensorDAO sensorDAO) {
        this.leituraDAO = leituraDAO;
        this.sensorDAO = sensorDAO;
    }

    @Transactional(readOnly = true)
    public List<LeituraSensorResponse> listar(Long sensorId) {
        List<LeituraSensor> lista = sensorId == null ? leituraDAO.findTop200ByOrderByDataHoraDesc()
                : leituraDAO.buscarPorSensor(sensorId);
        return lista.stream().map(LeituraSensorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public LeituraSensorResponse buscarPorId(Long id) {
        return LeituraSensorResponse.from(obter(id));
    }

    @Transactional
    public LeituraSensorResponse criar(LeituraSensorRequest req) {
        LeituraSensor leitura = new LeituraSensor();
        preencher(leitura, req);
        return LeituraSensorResponse.from(leituraDAO.save(leitura));
    }

    @Transactional
    public LeituraSensorResponse atualizar(Long id, LeituraSensorRequest req) {
        LeituraSensor leitura = obter(id);
        preencher(leitura, req);
        return LeituraSensorResponse.from(leituraDAO.save(leitura));
    }

    @Transactional
    public void excluir(Long id) {
        leituraDAO.delete(obter(id));
    }

    private void preencher(LeituraSensor leitura, LeituraSensorRequest req) {
        Sensor sensor = sensorDAO.findById(req.sensorId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Sensor não encontrado: id " + req.sensorId()));
        if (sensor.getStatus() != StatusSensor.ATIVO) {
            throw new RegraNegocioException("O sensor " + sensor.getCodigo() + " não está ATIVO e não pode registrar leituras.");
        }
        leitura.setSensor(sensor);
        leitura.setValor(req.valor());
        leitura.setUnidade(req.unidade().trim());
        leitura.setDataHora(req.dataHora() != null ? req.dataHora() : LocalDateTime.now());
    }

    private LeituraSensor obter(Long id) {
        return leituraDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitura não encontrada: id " + id));
    }
}
