package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

import br.com.jean.lolapirest.model.Campeao;
import br.com.jean.lolapirest.model.ClassePrincipal;

public class CampeaoDAO {

    public void inserirCampeao(Campeao campeao){
        String sql = "insert into campeao (nome, classe_principal) values (?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setString(1, campeao.getNome());
        inserir.setString(2, campeao.getClassePrincipal().name());
        inserir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir campeão.", e);
        }
    }

    public Campeao buscarPorId(Long id) {
        String sql = "select id, nome, classe_principal from campeao where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, id);
            ResultSet resultado = buscar.executeQuery();
            if (resultado.next()) {
                Long idEncontrado = resultado.getLong("id");
                String nome = resultado.getString("nome");
                ClassePrincipal classePrincipal = ClassePrincipal.valueOf(
                    resultado.getString("classe_principal")
                );

                Campeao campeao = new Campeao(nome, classePrincipal);
                campeao.setId(idEncontrado);

                return campeao;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar campeão por id.", e);
        }
    }

    public void atualizarCampeao(Campeao campeao) {
        String sql = "update campeao set nome = ?, classe_principal = ? where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement atualizar = conexao.prepareStatement(sql)) {

            atualizar.setString(1, campeao.getNome());
            atualizar.setString(2, campeao.getClassePrincipal().name());
            atualizar.setLong(3, campeao.getId());
            atualizar.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campeão.", e);
        }
    }

    public void excluirCampeao(Long id) {
        String sql = "delete from campeao where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement excluir = conexao.prepareStatement(sql)) {

            excluir.setLong(1, id);
            excluir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir campeão.", e);
        }
    }
}
