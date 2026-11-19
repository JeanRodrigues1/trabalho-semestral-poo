package br.com.jean.lolapirest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.model.Item;
import br.com.jean.lolapirest.model.TipoItem;

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

    public Item buscarPorId(Long id) {
        String sql = "select id, nome, custo, tipo from item where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement buscar = conexao.prepareStatement(sql)) {

            buscar.setLong(1, id);
            ResultSet resultado = buscar.executeQuery();
            if (resultado.next()) {
                Long idEncontrado = resultado.getLong("id");
                String nome = resultado.getString("nome");
                int custo = resultado.getInt("custo");
                TipoItem tipo = TipoItem.valueOf(resultado.getString("tipo"));

                Item item = new Item(nome, custo, tipo);
                item.setId(idEncontrado);

                return item;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item por id.", e);
        }
    }
    
    public void atualizarItem(Item item) {
        String sql = "update item set nome = ?, custo = ?, tipo = ? where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement atualizar = conexao.prepareStatement(sql)) {

            atualizar.setString(1, item.getNome());
            atualizar.setDouble(2, item.getCusto());
            atualizar.setString(3, item.getTipo().name());
            atualizar.setLong(4, item.getId());
            atualizar.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item.", e);
        }
    }

    public void excluirItem(Long id) {
        String sql = "delete from item where id = ?";

        try (Connection conexao = Database.getConnection();
            PreparedStatement excluir = conexao.prepareStatement(sql)) {

            excluir.setLong(1, id);
            excluir.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir item.", e);
        }
    }
}
