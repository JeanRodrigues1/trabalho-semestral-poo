package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import br.com.jean.lolapirest.config.Database;

import br.com.jean.lolapirest.model.Partida;
import br.com.jean.lolapirest.model.PartidaCasual;
import br.com.jean.lolapirest.model.PartidaRanqueada;

public class PartidaDAO {

    public void inserirPartida(Partida partida){
        String sql = "insert into partida (id, jogador_id, campeao_id, data, duracao_min, role, abates, mortes, assistencias, resultado, tipo, pdl_variacao, modo_jogo) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, partida.getId());
        inserir.setLong(2, partida.getJogador().getId());
        inserir.setLong(3, partida.getCampeao().getId());
        inserir.setTimestamp(4, Timestamp.valueOf(partida.getData()));
        inserir.setInt(5, partida.getDuracaoMin());
        inserir.setString(6, partida.getRole().name());
        inserir.setInt(7, partida.getAbates());
        inserir.setInt(8, partida.getMortes());
        inserir.setInt(9, partida.getAssistencias());
        inserir.setString(10, partida.getResultado().name());
        inserir.setString(11, partida.getTipo());
        if (partida instanceof PartidaRanqueada partidaRanqueada) {
            inserir.setInt(12, partidaRanqueada.getPdlVariacao());
            inserir.setNull(13, java.sql.Types.VARCHAR);
        } else if (partida instanceof PartidaCasual partidaCasual) {
            inserir.setNull(12, java.sql.Types.INTEGER);
            inserir.setString(13, partidaCasual.getModoJogo());
        } else {
            inserir.setNull(12, java.sql.Types.INTEGER);
            inserir.setNull(13, java.sql.Types.VARCHAR);
        }
        inserir.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir partida.", e);
        }
    }
}
