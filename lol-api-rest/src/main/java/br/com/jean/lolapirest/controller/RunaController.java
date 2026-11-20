package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.RunaDAO;
import br.com.jean.lolapirest.model.Caminho;
import br.com.jean.lolapirest.model.Runa;
import br.com.jean.lolapirest.model.TipoRuna;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RunaController implements HttpHandler {

    private final RunaDAO runaDAO = new RunaDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/runas".equals(path)) {
                criar(exchange);

            } else if ("GET".equals(metodo) && path.startsWith("/runas/")) {
                Long id = Long.parseLong(path.replace("/runas/", ""));
                buscarPorId(exchange, id);

            } else if ("PUT".equals(metodo) && path.startsWith("/runas/")) {
                Long id = Long.parseLong(path.replace("/runas/", ""));
                atualizar(exchange, id);

            } else if ("DELETE".equals(metodo) && path.startsWith("/runas/")) {
                Long id = Long.parseLong(path.replace("/runas/", ""));
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
        String caminho = extrairValor(corpo, "caminho");
        String tipo = extrairValor(corpo, "tipo");

        Runa runa = new Runa(nome, Caminho.valueOf(caminho), TipoRuna.valueOf(tipo));

        runaDAO.inserirRuna(runa);

        enviarResposta(exchange, 201, "{\"mensagem\": \"Runa criada\"}");
    }

    private void buscarPorId(HttpExchange exchange, Long id) throws IOException {
        Runa runa = runaDAO.buscarPorId(id);

        if (runa == null) {
            enviarResposta(exchange, 404, "{\"erro\": \"Runa não encontrada\"}");
            return;
        }

        String json = "{"
            + "\"id\": " + runa.getId() + ", "
            + "\"nome\": \"" + runa.getNome() + "\", "
            + "\"caminho\": \"" + runa.getCaminho() + "\", "
            + "\"tipo_runa\": \"" + runa.getTipo() + "\""
            + "}";

        enviarResposta(exchange, 200, json);
    }

    private void atualizar(HttpExchange exchange, Long id) throws IOException {
        String corpo = lerCorpo(exchange);

        String nome = extrairValor(corpo, "nome");
        String caminho = extrairValor(corpo, "caminho");
        String tipoRuna = extrairValor(corpo, "tipo_runa");

        Runa runa = new Runa(nome, Caminho.valueOf(caminho), TipoRuna.valueOf(tipoRuna));
        runa.setId(id);

        runaDAO.atualizarRuna(runa);

        enviarResposta(exchange, 200, "{\"mensagem\": \"Runa atualizada\"}");
    }

    private void deletar(HttpExchange exchange, Long id) throws IOException {
        runaDAO.excluirRuna(id);
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