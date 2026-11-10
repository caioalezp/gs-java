package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import br.com.fiap.citizentech.model.enums.NivelRisco;
import br.com.fiap.citizentech.model.enums.StatusAlerta;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "T_CT_ALERTA")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERTA")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NV_RISCO", nullable = false, length = 20)
    private NivelRisco nivel;

    @Column(name = "VL_PROBABILIDADE", nullable = false)
    private Double probabilidade;

    @Column(name = "VL_CONFIANCA", nullable = false)
    private Double confianca;

    @Column(name = "DS_MENSAGEM", nullable = false, length = 400)
    private String mensagem;

    @Column(name = "DS_ROTA_ALTERNATIVA", nullable = true, length = 300)
    private String rotaAlternativa;

    @Column(name = "DT_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_ALERTA", nullable = false, length = 20)
    private StatusAlerta status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MICRO_REGIAO", nullable = false)
    private MicroRegiao microRegiao;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_CT_ATENDIMENTO",
            joinColumns = @JoinColumn(name = "ID_ALERTA"),
            inverseJoinColumns = @JoinColumn(name = "ID_EQUIPE"))
    private Set<EquipeAtendimento> equipes = new HashSet<>();

    public Alerta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelRisco getNivel() {
        return nivel;
    }

    public void setNivel(NivelRisco nivel) {
        this.nivel = nivel;
    }

    public Double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(Double probabilidade) {
        this.probabilidade = probabilidade;
    }

    public Double getConfianca() {
        return confianca;
    }

    public void setConfianca(Double confianca) {
        this.confianca = confianca;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getRotaAlternativa() {
        return rotaAlternativa;
    }

    public void setRotaAlternativa(String rotaAlternativa) {
        this.rotaAlternativa = rotaAlternativa;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public StatusAlerta getStatus() {
        return status;
    }

    public void setStatus(StatusAlerta status) {
        this.status = status;
    }

    public MicroRegiao getMicroRegiao() {
        return microRegiao;
    }

    public void setMicroRegiao(MicroRegiao microRegiao) {
        this.microRegiao = microRegiao;
    }

    public Set<EquipeAtendimento> getEquipes() {
        return equipes;
    }

    public void setEquipes(Set<EquipeAtendimento> equipes) {
        this.equipes = equipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alerta outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
