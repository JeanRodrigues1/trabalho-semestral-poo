package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

import br.com.jean.lolapirest.model.Campeao;

public class CampeaoDAO {

    public void inserirCampeao(Campeao campeao){
        String sql = "insert into campeao (id, nome, classe_principal) values (?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setLong(1, campeao.getId());
        inserir.setString(2, campeao.getNome());
        inserir.setString(3, campeao.getClassePrincipal().name());
        inserir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir campeão.", e);
        }
    }
}
