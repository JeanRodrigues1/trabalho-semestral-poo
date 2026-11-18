package br.com.jean.lolapirest.model;

public class Runa{
    private Long id;
    private String nome;
    private Caminho caminho;
    private TipoRuna tipo;

    public Runa(String nome, Caminho caminho, TipoRuna tipo) {
        this.nome = nome;
        this.caminho = caminho;
        this.tipo = tipo;
    }

    public Long getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public Caminho getCaminho(){
        return caminho;
    }

    public TipoRuna getTipo(){
        return tipo;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCaminho(Caminho caminho){
        this.caminho = caminho;
    }

    public void setTipo(TipoRuna tipo){
        this.tipo = tipo;
    }
}