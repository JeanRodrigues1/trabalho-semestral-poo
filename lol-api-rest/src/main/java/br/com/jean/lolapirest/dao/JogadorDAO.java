package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import br.com.jean.lolapirest.model.Jogador;

public class JogadorDAO {

    public void inserirJogador(Jogador jogador){
        String sql = "insert into jogador (apelido, regiao, data_cadastro) values (?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql)) {

        inserir.setString(1, jogador.getApelido());
        inserir.setString(2, jogador.getRegiao());
        inserir.setTimestamp(3, Timestamp.valueOf(jogador.getDataCadastro()));
        inserir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir jogador.", e);
        }
    }

    public Jogador buscarPorId(Long id) {
        String sql = "select id, apelido, regiao, data_cadastro from jogador where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, id);
            ResultSet resultado = buscar.executeQuery();
            if (resultado.next()) {
                Long idEncontrado = resultado.getLong("id");
                String apelido = resultado.getString("apelido");
                String regiao = resultado.getString("regiao");
                LocalDateTime dataCadastro = resultado.getTimestamp("data_cadastro").toLocalDateTime();


                Jogador jogador = new Jogador(apelido, regiao, dataCadastro);
                jogador.setId(idEncontrado);

                return jogador;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar jogador por id.", e);
        }
    } 
    
    public void atualizarJogador(Jogador jogador) {
        String sql = "update jogador set apelido = ?, regiao = ? where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement atualizar = conexao.prepareStatement(sql)) {

            atualizar.setString(1, jogador.getApelido());
            atualizar.setString(2, jogador.getRegiao());
            atualizar.setLong(3, jogador.getId());
            atualizar.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar jogador.", e);
        }
    }

    public void excluirJogador(Long id) {
        String sql = "delete from jogador where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement excluir = conexao.prepareStatement(sql)) {

            excluir.setLong(1, id);
            excluir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir jogador.", e);
        }
    }
}
