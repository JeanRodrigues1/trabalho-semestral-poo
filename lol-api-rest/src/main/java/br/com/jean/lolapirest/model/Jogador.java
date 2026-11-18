package br.com.jean.lolapirest.model;

import java.time.LocalDateTime;

public class Jogador {
    private Long id;
    private String apelido;
    private String regiao;
    private final LocalDateTime dataCadastro; // data de cadastro, imutável, sem setter para edição.

    public Jogador(String apelido, String regiao, LocalDateTime dataCadastro) {
        this.apelido = apelido;
        this.regiao = regiao;
        this.dataCadastro = dataCadastro;
    }

    public String getApelido() {
        return apelido;
    }

    public String getRegiao() {
        return regiao;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setApelido(String apelido){
        this.apelido = apelido;
    }

    public void setRegiao(String regiao){
        this.regiao = regiao;
    }
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
}