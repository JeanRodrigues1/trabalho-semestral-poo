package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.JogadorDAO;
import br.com.jean.lolapirest.model.Jogador;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class JogadorController implements HttpHandler {

    private final JogadorDAO jogadorDAO = new JogadorDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/jogadores".equals(path)) {
                criar(exchange);

            } else if ("GET".equals(metodo) && path.startsWith("/jogadores/")) {
                Long id = Long.parseLong(path.replace("/jogadores/", ""));
                buscarPorId(exchange, id);

            } else if ("PUT".equals(metodo) && path.startsWith("/jogadores/")) {
                Long id = Long.parseLong(path.replace("/jogadores/", ""));
                atualizar(exchange, id);

            } else if ("DELETE".equals(metodo) && path.startsWith("/jogadores/")) {
                Long id = Long.parseLong(path.replace("/jogadores/", ""));
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

        String apelido = extrairValor(corpo, "apelido");
        String regiao = extrairValor(corpo, "regiao");

        Jogador jogador = new Jogador(apelido, regiao, LocalDateTime.now());
        jogador.setId(System.currentTimeMillis());

        jogadorDAO.inserirJogador(jogador);

        enviarResposta(exchange, 201, "{\"mensagem\": \"Jogador criado\"}");
    }

    private void buscarPorId(HttpExchange exchange, Long id) throws IOException {
        Jogador jogador = jogadorDAO.buscarPorId(id);

        if (jogador == null) {
            enviarResposta(exchange, 404, "{\"erro\": \"Jogador não encontrado\"}");
            return;
        }

        String json = "{"
            + "\"id\": " + jogador.getId() + ", "
            + "\"apelido\": \"" + jogador.getApelido() + "\", "
            + "\"regiao\": \"" + jogador.getRegiao() + "\""
            + "}";

        enviarResposta(exchange, 200, json);
    }

    private void atualizar(HttpExchange exchange, Long id) throws IOException {
        String corpo = lerCorpo(exchange);

        String apelido = extrairValor(corpo, "apelido");
        String regiao = extrairValor(corpo, "regiao");

        Jogador jogador = new Jogador(apelido, regiao, LocalDateTime.now());
        jogador.setId(id);

        jogadorDAO.atualizarJogador(jogador);

        enviarResposta(exchange, 200, "{\"mensagem\": \"Jogador atualizado\"}");
    }

    private void deletar(HttpExchange exchange, Long id) throws IOException {
        jogadorDAO.excluirJogador(id);

        enviarResposta(exchange, 204, "");
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