package br.com.jean.lolapirest.model;

import java.time.LocalDateTime;

public class PartidaRanqueada extends Partida{
    private int pdlVariacao;

    public PartidaRanqueada(Jogador jogador, Campeao campeao, LocalDateTime data, int duracaoMin, Role role, int abates, int mortes,
     int assistencias, Resultado resultado, int pdlVariacao) {
        super(jogador, campeao, data, duracaoMin, role, abates, mortes, assistencias, resultado);
        this.pdlVariacao = pdlVariacao;
    }

    public int getPdlVariacao() {
        return pdlVariacao;
    }

    public void setPdlVariacao(int pdlVariacao) {
        this.pdlVariacao = pdlVariacao;
    }

    @Override
    public String getTipo() {
        return "RANQUEADA";
    }
}