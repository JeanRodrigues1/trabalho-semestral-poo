package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.model.Campeao;
import br.com.jean.lolapirest.model.Jogador;
import br.com.jean.lolapirest.model.Partida;
import br.com.jean.lolapirest.model.PartidaCasual;
import br.com.jean.lolapirest.model.PartidaRanqueada;
import br.com.jean.lolapirest.model.Resultado;
import br.com.jean.lolapirest.model.Role;

public class PartidaDAO {
    private final JogadorDAO jogadorDAO = new JogadorDAO();
    private final CampeaoDAO campeaoDAO = new CampeaoDAO();


    public void inserirPartida(Partida partida){
        String sql = "insert into partida (jogador_id, campeao_id, data, duracao_min, role, abates, mortes, assistencias, resultado, tipo, pdl_variacao, modo_jogo) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = Database.getConnection();
            PreparedStatement inserir = conexao.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        inserir.setLong(1, partida.getJogador().getId());
        inserir.setLong(2, partida.getCampeao().getId());
        inserir.setTimestamp(3, Timestamp.valueOf(partida.getData()));
        inserir.setInt(4, partida.getDuracaoMin());
        inserir.setString(5, partida.getRole().name());
        inserir.setInt(6, partida.getAbates());
        inserir.setInt(7, partida.getMortes());
        inserir.setInt(8, partida.getAssistencias());
        inserir.setString(9, partida.getResultado().name());
        inserir.setString(10, partida.getTipo());
        if (partida instanceof PartidaRanqueada partidaRanqueada) {
            inserir.setInt(11, partidaRanqueada.getPdlVariacao());
            inserir.setNull(12, java.sql.Types.VARCHAR);
        } else if (partida instanceof PartidaCasual partidaCasual) {
            inserir.setNull(11, java.sql.Types.INTEGER);
            inserir.setString(12, partidaCasual.getModoJogo());
        } else {
            inserir.setNull(11, java.sql.Types.INTEGER);
            inserir.setNull(12, java.sql.Types.VARCHAR);
        }
        inserir.executeUpdate();

        try (ResultSet chaves = inserir.getGeneratedKeys()) {
            if (chaves.next()) {
                partida.setId(chaves.getLong(1));
            }
        }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir partida.", e);
        }
    }

    public Partida buscarPorId(Long id) {
        String sql = "select id, jogador_id, campeao_id, data, duracao_min, role, abates, mortes, assistencias, resultado, tipo, pdl_variacao, modo_jogo from partida where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, id);
            ResultSet resultado = buscar.executeQuery();

            if (resultado.next()) {
                Jogador jogador = jogadorDAO.buscarPorId(resultado.getLong("jogador_id"));
                Campeao campeao = campeaoDAO.buscarPorId(resultado.getLong("campeao_id"));

                LocalDateTime data = resultado.getTimestamp("data").toLocalDateTime();
                Role role = Role.valueOf(resultado.getString("role"));
                Resultado resultadoPartida = Resultado.valueOf(resultado.getString("resultado"));
                int duracaoMin = resultado.getInt("duracao_min");
                int abates = resultado.getInt("abates");
                int mortes = resultado.getInt("mortes");
                int assistencias = resultado.getInt("assistencias");

                Integer pdlVariacao = null;
                if (resultado.getObject("pdl_variacao") != null) {
                    pdlVariacao = resultado.getInt("pdl_variacao");
                }

                Partida partida;
                if (pdlVariacao != null) {
                    partida = new PartidaRanqueada(
                        jogador, campeao, data, duracaoMin, role,
                        abates, mortes, assistencias, resultadoPartida, pdlVariacao
                    );
                } else {
                    partida = new PartidaCasual(
                        jogador, campeao, data, duracaoMin, role,
                        abates, mortes, assistencias, resultadoPartida,
                        resultado.getString("modo_jogo")
                    );
                }
                partida.setId(resultado.getLong("id"));
                return partida;
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar partida por id.", e);
        }
    }

    public java.util.List<Partida> listarPorJogador(Long jogadorId) {
        String sql = "select id, jogador_id, campeao_id, data, duracao_min, role, abates, mortes, assistencias, resultado, tipo, pdl_variacao, modo_jogo from partida where jogador_id = ? order by id";

        java.util.List<Partida> partidas = new java.util.ArrayList<>();

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, jogadorId);
            ResultSet resultado = buscar.executeQuery();

            while (resultado.next()) {
                Jogador jogador = jogadorDAO.buscarPorId(resultado.getLong("jogador_id"));
                Campeao campeao = campeaoDAO.buscarPorId(resultado.getLong("campeao_id"));

                LocalDateTime data = resultado.getTimestamp("data").toLocalDateTime();
                Role role = Role.valueOf(resultado.getString("role"));
                Resultado resultadoPartida = Resultado.valueOf(resultado.getString("resultado"));

                Integer pdlVariacao = null;
                if (resultado.getObject("pdl_variacao") != null) {
                    pdlVariacao = resultado.getInt("pdl_variacao");
                }

                Partida partida;
                if (pdlVariacao != null) {
                    partida = new PartidaRanqueada(
                        jogador, campeao, data, resultado.getInt("duracao_min"), role,
                        resultado.getInt("abates"), resultado.getInt("mortes"),
                        resultado.getInt("assistencias"), resultadoPartida, pdlVariacao
                    );
                } else {
                    partida = new PartidaCasual(
                        jogador, campeao, data, resultado.getInt("duracao_min"), role,
                        resultado.getInt("abates"), resultado.getInt("mortes"),
                        resultado.getInt("assistencias"), resultadoPartida,
                        resultado.getString("modo_jogo")
                    );
                }

                partida.setId(resultado.getLong("id"));
                partidas.add(partida);
            }

            return partidas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar partidas do jogador.", e);
        }
    }
}
