package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_CT_LEITURA_SENSOR")
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEITURA")
    private Long id;

    @Column(name = "VL_LEITURA", nullable = false)
    private Double valor;

    @Column(name = "DS_UNIDADE", nullable = false, length = 20)
    private String unidade;

    @Column(name = "DT_LEITURA", nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_SENSOR", nullable = false)
    private Sensor sensor;

    public LeituraSensor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeituraSensor outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
