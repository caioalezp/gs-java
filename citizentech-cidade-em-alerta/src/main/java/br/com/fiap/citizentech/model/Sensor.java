package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import br.com.fiap.citizentech.model.enums.StatusSensor;
import br.com.fiap.citizentech.model.enums.TipoSensor;

@Entity
@Table(name = "T_CT_SENSOR")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SENSOR")
    private Long id;

    @Column(name = "CD_SENSOR", nullable = false, length = 50, unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_SENSOR", nullable = false, length = 20)
    private TipoSensor tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_SENSOR", nullable = false, length = 20)
    private StatusSensor status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MICRO_REGIAO", nullable = false)
    private MicroRegiao microRegiao;

    public Sensor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoSensor getTipo() {
        return tipo;
    }

    public void setTipo(TipoSensor tipo) {
        this.tipo = tipo;
    }

    public StatusSensor getStatus() {
        return status;
    }

    public void setStatus(StatusSensor status) {
        this.status = status;
    }

    public MicroRegiao getMicroRegiao() {
        return microRegiao;
    }

    public void setMicroRegiao(MicroRegiao microRegiao) {
        this.microRegiao = microRegiao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
