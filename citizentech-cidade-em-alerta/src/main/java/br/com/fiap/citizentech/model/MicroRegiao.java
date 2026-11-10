package br.com.fiap.citizentech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "T_CT_MICRO_REGIAO")
public class MicroRegiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MICRO_REGIAO")
    private Long id;

    @Column(name = "NM_MICRO_REGIAO", nullable = false, length = 150)
    private String nome;

    @Column(name = "VL_LATITUDE", nullable = false)
    private Double latitude;

    @Column(name = "VL_LONGITUDE", nullable = false)
    private Double longitude;

    @Column(name = "NR_CAPACIDADE_ESCOAMENTO", nullable = false)
    private Integer capacidadeEscoamento;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_REGIAO", nullable = false)
    private Regiao regiao;

    public MicroRegiao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getCapacidadeEscoamento() {
        return capacidadeEscoamento;
    }

    public void setCapacidadeEscoamento(Integer capacidadeEscoamento) {
        this.capacidadeEscoamento = capacidadeEscoamento;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MicroRegiao outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
