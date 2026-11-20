package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.model.Item;
import br.com.jean.lolapirest.model.TipoItem;


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
    public java.util.List<Item> listarItensPorPartida(Long partidaId) {
        String sql = "select i.id, i.nome, i.custo, i.tipo from partida_item pi join item i on pi.item_id = i.id where pi.partida_id = ?";

        java.util.List<Item> itens = new java.util.ArrayList<>();

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, partidaId);
            ResultSet resultado = buscar.executeQuery();

            while (resultado.next()) {
                Item item = new Item(
                    resultado.getString("nome"),
                    resultado.getInt("custo"),
                    TipoItem.valueOf(resultado.getString("tipo"))
                );
                item.setId(resultado.getLong("id"));
                itens.add(item);
            }

            return itens;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens da partida.", e);
        }
    }
}
