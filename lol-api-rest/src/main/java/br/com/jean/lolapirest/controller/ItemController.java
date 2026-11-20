package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.ItemDAO;
import br.com.jean.lolapirest.model.Item;
import br.com.jean.lolapirest.model.TipoItem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ItemController implements HttpHandler {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/itens".equals(path)) {
                criar(exchange);

            } else if ("GET".equals(metodo) && path.startsWith("/itens/")) {
                Long id = Long.parseLong(path.replace("/itens/", ""));
                buscarPorId(exchange, id);

            } else if ("PUT".equals(metodo) && path.startsWith("/itens/")) {
                Long id = Long.parseLong(path.replace("/itens/", ""));
                atualizar(exchange, id);

            } else if ("DELETE".equals(metodo) && path.startsWith("/itens/")) {
                Long id = Long.parseLong(path.replace("/itens/", ""));
                deletar(exchange, id);

            } else {
                enviarResposta(exchange, 404, "{\"erro\": \"Rota não encontrada\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            enviarResposta(exchange, 500, "{\"erro\": \"Erro interno no servidor\"}");
        }
    }

    private void criar(HttpExchange exchange) throws IOException {
        String corpo = lerCorpo(exchange);

        String nome = extrairValor(corpo, "nome");
        int custo = Integer.parseInt(extrairValor(corpo, "custo"));
        String tipoItem = extrairValor(corpo, "tipo_item");

        Item item = new Item(nome, custo, TipoItem.valueOf(tipoItem));

        itemDAO.inserirItem(item);

        enviarResposta(exchange, 201, "{\"mensagem\": \"Item criado\"}");
    }

    private void buscarPorId(HttpExchange exchange, Long id) throws IOException {
        Item item = itemDAO.buscarPorId(id);

        if (item == null) {
            enviarResposta(exchange, 404, "{\"erro\": \"Item não encontrado\"}");
            return;
        }

        String json = "{"
            + "\"id\": " + item.getId() + ", "
            + "\"nome\": \"" + item.getNome() + "\", "
            + "\"custo\": " + item.getCusto() + ", "
            + "\"tipo_item\": \"" + item.getTipo() + "\""
            + "}";

        enviarResposta(exchange, 200, json);
    }

    private void atualizar(HttpExchange exchange, Long id) throws IOException {
        String corpo = lerCorpo(exchange);

        String nome = extrairValor(corpo, "nome");
        int custo = Integer.parseInt(extrairValor(corpo, "custo"));
        String tipoItem = extrairValor(corpo, "tipo_item");

        Item item = new Item(nome, custo, TipoItem.valueOf(tipoItem));
        item.setId(id);

        itemDAO.atualizarItem(item);

        enviarResposta(exchange, 200, "{\"mensagem\": \"Item atualizado\"}");
    }

    private void deletar(HttpExchange exchange, Long id) throws IOException {
        itemDAO.excluirItem(id);
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private String lerCorpo(HttpExchange exchange) throws IOException {
        return new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );
    }

    private String extrairValor(String json, String campo) {
        String chave = "\"" + campo + "\":\"";

        int inicio = json.indexOf(chave) + chave.length();
        int fim = json.indexOf("\"", inicio);

        return json.substring(inicio, fim);
    }

    private void enviarResposta(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(bytes);
        }
    }
}