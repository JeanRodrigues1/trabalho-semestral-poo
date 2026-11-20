package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.model.Caminho;
import br.com.jean.lolapirest.model.Runa;
import br.com.jean.lolapirest.model.TipoRuna;

public class RunaDAO {

    public void inserirRuna(Runa runa){
        String sql = "insert into runa (nome, caminho, tipo) values (?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setString(1, runa.getNome());
        inserir.setString(2, runa.getCaminho().name());
        inserir.setString(3, runa.getTipo().name());

        inserir.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir runa.", e);
        }
    }

    public Runa buscarPorId(Long id) {
    String sql = "select id, nome, caminho, tipo from runa where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {
            buscar.setLong(1, id);
            ResultSet resultado = buscar.executeQuery();
            if (resultado.next()) {
                Long idEncontrado = resultado.getLong("id");
                String nome = resultado.getString("nome");
                String caminho = resultado.getString("caminho");
                String tipo = resultado.getString("tipo");


                Runa runa = new Runa(nome, Caminho.valueOf(caminho), TipoRuna.valueOf(tipo));
                runa.setId(idEncontrado);

                return runa;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar runa por id.", e);
        }
    }

    public void atualizarRuna(Runa runa) {
        String sql = "update runa set nome = ?, caminho = ?, tipo = ? where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement atualizar = conexao.prepareStatement(sql)) {

            atualizar.setString(1, runa.getNome());
            atualizar.setString(2, runa.getTipo().name());
            atualizar.setString(3, runa.getCaminho().name());
            atualizar.setLong(4, runa.getId());
            atualizar.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar runa.", e);
        }
    }

    public void excluirRuna(Long id) {
        String sql = "delete from runa where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement excluir = conexao.prepareStatement(sql)) {

            excluir.setLong(1, id);
            excluir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir runa.", e);
        }
    }
}
