package br.com.jean.lolapirest.model;

public class Campeao {
    private Long id;
    private String nome;
    private ClassePrincipal classePrincipal;

    public Campeao(String nome, ClassePrincipal classePrincipal) {
        this.nome = nome;
        this.classePrincipal = classePrincipal;
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

    public ClassePrincipal getClassePrincipal() {
        return classePrincipal;
    }

    public void setClassePrincipal(ClassePrincipal classePrincipal) {
        this.classePrincipal = classePrincipal;
    }
}