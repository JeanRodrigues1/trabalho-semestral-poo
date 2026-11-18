package br.com.jean.lolapirest.model;

import java.time.LocalDateTime;

public abstract class Partida {
    private Long id;
    private Jogador jogador;
    private Campeao campeao;
    private LocalDateTime data;
    private int duracaoMin; //armazena os minutos apenas, ignora segundos
    private Role role;
    private int abates;
    private int mortes;
    private int assistencias;
    private Resultado resultado;

    public Partida(Jogador jogador, Campeao campeao, LocalDateTime data, int duracaoMin, 
    Role role, int abates, int mortes, int assistencias, Resultado resultado) {
        this.jogador = jogador;
        this.campeao = campeao;
        this.data = data;
        this.duracaoMin = duracaoMin;
        this.role = role;
        this.abates = abates;
        this.mortes = mortes;
        this.assistencias = assistencias;
        this.resultado = resultado;
    }

    public Long getId(){
        return id;
    }

    public Jogador getJogador(){
        return jogador;
    }

    public Campeao getCampeao(){
        return campeao;
    }

    public LocalDateTime getData(){
        return data;
    }

    public int getDuracaoMin(){
        return duracaoMin;
    }

    public Role getRole(){
        return role;
    }

    public int getAbates(){
        return abates;
    }

    public int getMortes(){
        return mortes;
    }

    public int getAssistencias(){
        return assistencias;
    }

    public Resultado getResultado(){
        return resultado;
    }

    public void setId(Long id){
        this.id = id;
    } 

    public void setJogador(Jogador jogador){
        this.jogador = jogador;
    }

    public void setCampeao(Campeao campeao){
        this.campeao = campeao;
    }

    public void setData(LocalDateTime data){
        this.data = data;
    }

    public void setDuracaoMin(int duracaoMin){
        this.duracaoMin = duracaoMin;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public void setAbates(int abates){
        this.abates = abates;
    }

    public void setMortes(int mortes){
        this.mortes = mortes;
    }

    public void setAssistencias(int assistencias){
        this.assistencias = assistencias;
    }

    public void setResultado(Resultado resultado){
        this.resultado = resultado;
    }

    public abstract String getTipo(); 

}
