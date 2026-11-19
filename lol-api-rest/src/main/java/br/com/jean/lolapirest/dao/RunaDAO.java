package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

import br.com.jean.lolapirest.model.Runa;

public class RunaDAO {

    public void inserirRuna(Runa runa){
        String sql = "insert into runa (id, nome, tipo, caminho) values (?, ?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, runa.getId());
        inserir.setString(2, runa.getNome());
        inserir.setString(3, runa.getTipo().name());
        inserir.setString(4, runa.getCaminho().name());
        inserir.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir runa.", e);
        }
    }
}
