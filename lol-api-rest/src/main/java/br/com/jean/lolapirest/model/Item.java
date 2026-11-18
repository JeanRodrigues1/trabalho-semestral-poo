package br.com.jean.lolapirest.model;

public class Item{
    private Long id;
    private String nome;
    private int custo;
    private TipoItem tipo;

    public Item(String nome, int custo, TipoItem tipo) {
        this.nome = nome;
        this.custo = custo;
        this.tipo = tipo;
    }

    public Long getId(){
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCusto() {
        return custo;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public void setTipo(TipoItem tipo) {
        this.tipo = tipo;
    }
}