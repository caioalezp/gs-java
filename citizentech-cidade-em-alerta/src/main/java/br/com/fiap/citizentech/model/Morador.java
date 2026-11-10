package br.com.fiap.citizentech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "T_CT_MORADOR")
public class Morador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MORADOR")
    private Long id;

    @Column(name = "NM_MORADOR", nullable = false, length = 120)
    private String nome;

    @Column(name = "DS_EMAIL", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "NR_TELEFONE", nullable = true, length = 20)
    private String telefone;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MICRO_REGIAO", nullable = false)
    private MicroRegiao microRegiao;

    public Morador() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
        if (!(o instanceof Morador outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
