package br.com.fiap.citizentech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "T_CT_REGIAO")
public class Regiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGIAO")
    private Long id;

    @Column(name = "NM_REGIAO", nullable = false, length = 100)
    private String nome;

    @Column(name = "NM_CIDADE", nullable = false, length = 100)
    private String cidade;

    public Regiao() {
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regiao outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
