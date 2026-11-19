    package br.com.jean.lolapirest.dao;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    import br.com.jean.lolapirest.config.Database;

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
    }
