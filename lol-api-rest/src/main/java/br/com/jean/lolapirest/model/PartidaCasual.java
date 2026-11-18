package br.com.jean.lolapirest.model;

import java.time.LocalDateTime;

public class PartidaCasual extends Partida{
    private String modoJogo;

    public PartidaCasual(Jogador jogador, Campeao campeao, LocalDateTime data, int duracaoMin, Role role, int abates, int mortes,
     int assistencias, Resultado resultado, String modoJogo) {
        super(jogador, campeao, data, duracaoMin, role, abates, mortes, assistencias, resultado);
        this.modoJogo = modoJogo ;
    }

    public String getModoJogo() {
        return modoJogo;
    }

    public void setModoJogo(String modoJogo) {
        this.modoJogo = modoJogo;
    }
    
    @Override
    public String getTipo() {
        return "CASUAL";
    }
}