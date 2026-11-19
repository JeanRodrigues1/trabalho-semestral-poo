package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

import br.com.jean.lolapirest.model.Item;

public class ItemDAO {

    public void inserirItem(Item item){
        String sql = "insert into item (id, nome, custo, tipo) values (?, ?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, item.getId());
        inserir.setString(2, item.getNome());
        inserir.setDouble(3, item.getCusto());
        inserir.setString(4, item.getTipo().name());
        inserir.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item.", e);
        }
    }
}
