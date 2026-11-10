package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "T_CT_HISTORICO_ALAGAMENTO")
public class HistoricoAlagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORICO")
    private Long id;

    @Column(name = "DT_OCORRENCIA", nullable = false)
    private LocalDate dataOcorrencia;

    @Column(name = "NR_GRAVIDADE", nullable = false)
    private Integer gravidade;

    @Column(name = "NR_DURACAO_MIN", nullable = false)
    private Integer duracaoMinutos;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MICRO_REGIAO", nullable = false)
    private MicroRegiao microRegiao;

    public HistoricoAlagamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public Integer getGravidade() {
        return gravidade;
    }

    public void setGravidade(Integer gravidade) {
        this.gravidade = gravidade;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
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
        if (!(o instanceof HistoricoAlagamento outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
