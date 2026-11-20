package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.model.Caminho;
import br.com.jean.lolapirest.model.Runa;
import br.com.jean.lolapirest.model.TipoRuna;

    public class PartidaRunaDAO {

        public void inserirPartidaRuna(Long partidaId, Long runaId){
            String sql = "insert into partida_runa (partida_id, runa_id) values (?, ?)";

            try (Connection conexao = Database.getConnection();
                PreparedStatement inserir = conexao.prepareStatement(sql)) {

            inserir.setLong(1, partidaId);
            inserir.setLong(2, runaId);
            inserir.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir partida runa.", e);
            }
        }
        public java.util.List<Runa> listarRunasPorPartida(Long partidaId) {
            String sql = "select r.id, r.nome, r.caminho, r.tipo from partida_runa pr join runa r on pr.runa_id = r.id where pr.partida_id = ?";

            java.util.List<Runa> runas = new java.util.ArrayList<>();

            try (Connection conexao = Database.getConnection();
                PreparedStatement buscar = conexao.prepareStatement(sql)) {

                buscar.setLong(1, partidaId);
                ResultSet resultado = buscar.executeQuery();

                while (resultado.next()) {
                    Runa runa = new Runa(
                        resultado.getString("nome"),
                        Caminho.valueOf(resultado.getString("caminho")),
                        TipoRuna.valueOf(resultado.getString("tipo"))
                    );
                    runa.setId(resultado.getLong("id"));
                    runas.add(runa);
                }

                return runas;

            } catch (SQLException e) {
                throw new RuntimeException("Erro ao listar runas da partida.", e);
            }
        }
    }
