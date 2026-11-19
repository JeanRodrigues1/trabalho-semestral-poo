package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

public class PartidaItemDAO {

    public void inserirPartidaItem(Long partidaId, Long itemId){
        String sql = "insert into partida_item (partida_id, item_id) values (?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, partidaId);
        inserir.setLong(2, itemId);
        inserir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir partida item.", e);
        }
    }
}
