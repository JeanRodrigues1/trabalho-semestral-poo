package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import java.sql.Timestamp;

import br.com.jean.lolapirest.model.Jogador;

public class JogadorDAO {

    public void inserirJogador(Jogador jogador){
        String sql = "insert into jogador (id, apelido, regiao, data_cadastro) values (?, ?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, jogador.getId());
        inserir.setString(2, jogador.getApelido());
        inserir.setString(3, jogador.getRegiao());
        inserir.setTimestamp(4, Timestamp.valueOf(jogador.getDataCadastro()));
        inserir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir jogador.", e);
        }
    }
}
