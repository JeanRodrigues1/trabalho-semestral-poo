package br.com.jean.lolapirest.service;

import br.com.jean.lolapirest.dao.ItemDAO;
import br.com.jean.lolapirest.dao.RunaDAO;
import br.com.jean.lolapirest.model.Item;
import br.com.jean.lolapirest.model.Runa;
import br.com.jean.lolapirest.model.TipoItem;
import br.com.jean.lolapirest.model.TipoRuna;

public class PartidaService {

    private final RunaDAO runaDAO = new RunaDAO();
    private final ItemDAO itemDAO = new ItemDAO();

    public void validarRunas(String idsRunas) {
        String[] ids = idsRunas.split(",");

        if (ids.length != 6) {
            throw new IllegalArgumentException("A partida deve conter exatamente 6 runas");
        }

        int chaves = 0;
        int menores = 0;

        for (String id : ids) {
            Runa runa = runaDAO.buscarPorId(Long.parseLong(id.trim()));

            if (runa == null) {
                throw new IllegalArgumentException("Runa não encontrada");
            }

            if (runa.getTipo() == TipoRuna.CHAVE) {
                chaves++;
            } else if (runa.getTipo() == TipoRuna.MENOR) {
                menores++;
            }
        }

        if (chaves != 2 || menores != 4) {
            throw new IllegalArgumentException("A partida deve conter 2 runas CHAVE e 4 runas MENOR");
        }
    }

    public void validarItens(String idsItens) {
        String[] ids = idsItens.split(",");

        if (ids.length > 6) {
            throw new IllegalArgumentException("A partida pode ter no máximo 6 itens");
        }

        int botas = 0;

        for (String id : ids) {
            Item item = itemDAO.buscarPorId(Long.parseLong(id.trim()));

            if (item == null) {
                throw new IllegalArgumentException("Item não encontrado");
            }

            if (item.getTipo() == TipoItem.BOTA) {
                botas++;
            }
        }

        if (botas > 1) {
            throw new IllegalArgumentException("A partida pode ter no máximo 1 item do tipo BOTA");
        }
    }
}