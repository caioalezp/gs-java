package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import br.com.fiap.citizentech.model.enums.StatusRelato;
import br.com.fiap.citizentech.model.enums.TipoRelato;

@Entity
@Table(name = "T_CT_RELATO")
public class Relato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RELATO")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_RELATO", nullable = false, length = 20)
    private TipoRelato tipo;

    @Column(name = "DS_RELATO", nullable = false, length = 500)
    private String descricao;

    @Column(name = "DT_RELATO", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_RELATO", nullable = false, length = 20)
    private StatusRelato status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MORADOR", nullable = false)
    private Morador morador;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MICRO_REGIAO", nullable = false)
    private MicroRegiao microRegiao;

    public Relato() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRelato getTipo() {
        return tipo;
    }

    public void setTipo(TipoRelato tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusRelato getStatus() {
        return status;
    }

    public void setStatus(StatusRelato status) {
        this.status = status;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
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
        if (!(o instanceof Relato outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
