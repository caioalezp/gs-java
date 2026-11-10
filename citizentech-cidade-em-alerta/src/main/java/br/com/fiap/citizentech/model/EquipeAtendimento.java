package br.com.fiap.citizentech.model;

import jakarta.persistence.*;
import br.com.fiap.citizentech.model.enums.DisponibilidadeEquipe;

@Entity
@Table(name = "T_CT_EQUIPE_ATENDIMENTO")
public class EquipeAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPE")
    private Long id;

    @Column(name = "NM_EQUIPE", nullable = false, length = 100)
    private String nome;

    @Column(name = "DS_ESPECIALIDADE", nullable = false, length = 100)
    private String especialidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_DISPONIBILIDADE", nullable = false, length = 20)
    private DisponibilidadeEquipe disponibilidade;

    public EquipeAtendimento() {
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public DisponibilidadeEquipe getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(DisponibilidadeEquipe disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipeAtendimento outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
