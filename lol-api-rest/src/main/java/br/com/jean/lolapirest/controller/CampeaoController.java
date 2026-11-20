package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.CampeaoDAO;
import br.com.jean.lolapirest.model.Campeao;
import br.com.jean.lolapirest.model.ClassePrincipal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CampeaoController implements HttpHandler {

    private final CampeaoDAO campeaoDAO = new CampeaoDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/campeoes".equals(path)) {
                criar(exchange);

            } else if ("GET".equals(metodo) && path.startsWith("/campeoes/")) {
                Long id = Long.parseLong(path.replace("/campeoes/", ""));
                buscarPorId(exchange, id);

            } else if ("PUT".equals(metodo) && path.startsWith("/campeoes/")) {
                Long id = Long.parseLong(path.replace("/campeoes/", ""));
                atualizar(exchange, id);

            } else if ("DELETE".equals(metodo) && path.startsWith("/campeoes/")) {
                Long id = Long.parseLong(path.replace("/campeoes/", ""));
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
        String classePrincipal = extrairValor(corpo, "classe_principal");

        Campeao campeao = new Campeao(nome, ClassePrincipal.valueOf(classePrincipal));

        campeaoDAO.inserirCampeao(campeao);

        enviarResposta(exchange, 201, "{\"mensagem\": \"Campeão criado\"}");
    }

    private void buscarPorId(HttpExchange exchange, Long id) throws IOException {
        Campeao campeao = campeaoDAO.buscarPorId(id);

        if (campeao == null) {
            enviarResposta(exchange, 404, "{\"erro\": \"Campeão não encontrado\"}");
            return;
        }

        String json = "{"
            + "\"id\": " + campeao.getId() + ", "
            + "\"nome\": \"" + campeao.getNome() + "\", "
            + "\"classe_principal\": \"" + campeao.getClassePrincipal() + "\""
            + "}";

        enviarResposta(exchange, 200, json);
    }

    private void atualizar(HttpExchange exchange, Long id) throws IOException {
        String corpo = lerCorpo(exchange);

        String nome = extrairValor(corpo, "nome");
        String classePrincipal = extrairValor(corpo, "classe_principal");

        Campeao campeao = new Campeao(nome, ClassePrincipal.valueOf(classePrincipal));
        campeao.setId(id);

        campeaoDAO.atualizarCampeao(campeao);

        enviarResposta(exchange, 200, "{\"mensagem\": \"Campeão atualizado\"}");
    }

    private void deletar(HttpExchange exchange, Long id) throws IOException {
        campeaoDAO.excluirCampeao(id);
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