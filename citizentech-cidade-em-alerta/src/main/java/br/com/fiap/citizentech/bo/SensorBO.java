package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.LeituraSensorDAO;
import br.com.fiap.citizentech.dao.MicroRegiaoDAO;
import br.com.fiap.citizentech.dao.SensorDAO;
import br.com.fiap.citizentech.dto.SensorRequest;
import br.com.fiap.citizentech.dto.SensorResponse;
import br.com.fiap.citizentech.exception.ConflitoException;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraNegocioException;
import br.com.fiap.citizentech.model.MicroRegiao;
import br.com.fiap.citizentech.model.Sensor;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorBO {

    private final SensorDAO sensorDAO;
    private final MicroRegiaoDAO microRegiaoDAO;
    private final LeituraSensorDAO leituraDAO;

    public SensorBO(SensorDAO sensorDAO, MicroRegiaoDAO microRegiaoDAO, LeituraSensorDAO leituraDAO) {
        this.sensorDAO = sensorDAO;
        this.microRegiaoDAO = microRegiaoDAO;
        this.leituraDAO = leituraDAO;
    }

    @Transactional(readOnly = true)
    public List<SensorResponse> listar(Long microRegiaoId) {
        List<Sensor> lista = microRegiaoId == null ? sensorDAO.findAll() : sensorDAO.findByMicroRegiaoId(microRegiaoId);
        return lista.stream().map(SensorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SensorResponse buscarPorId(Long id) {
        return SensorResponse.from(obter(id));
    }

    @Transactional
    public SensorResponse criar(SensorRequest req) {
        String codigo = req.codigo().trim();
        if (sensorDAO.existsByCodigo(codigo)) {
            throw new ConflitoException("Já existe um sensor com o código " + codigo);
        }
        Sensor sensor = new Sensor();
        preencher(sensor, req);
        return SensorResponse.from(sensorDAO.save(sensor));
    }

    @Transactional
    public SensorResponse atualizar(Long id, SensorRequest req) {
        Sensor sensor = obter(id);
        if (sensorDAO.existsByCodigoAndIdNot(req.codigo().trim(), id)) {
            throw new ConflitoException("Já existe outro sensor com o código " + req.codigo().trim());
        }
        preencher(sensor, req);
        return SensorResponse.from(sensorDAO.save(sensor));
    }

    @Transactional
    public void excluir(Long id) {
        Sensor sensor = obter(id);
        if (leituraDAO.existsBySensorId(id)) {
            throw new RegraNegocioException("Não é possível excluir o sensor: ele possui leituras registradas. Marque-o como INATIVO.");
        }
        sensorDAO.delete(sensor);
    }

    private void preencher(Sensor sensor, SensorRequest req) {
        MicroRegiao micro = microRegiaoDAO.findById(req.microRegiaoId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Microrregião não encontrada: id " + req.microRegiaoId()));
        sensor.setCodigo(req.codigo().trim());
        sensor.setTipo(req.tipo());
        sensor.setStatus(req.status());
        sensor.setMicroRegiao(micro);
    }

    private Sensor obter(Long id) {
        return sensorDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sensor não encontrado: id " + id));
    }
}
