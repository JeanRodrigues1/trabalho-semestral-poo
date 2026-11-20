package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.JogadorDAO;
import br.com.jean.lolapirest.dao.PartidaDAO;
import br.com.jean.lolapirest.model.Jogador;
import br.com.jean.lolapirest.model.Partida;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class JogadorController implements HttpHandler {

    private final JogadorDAO jogadorDAO = new JogadorDAO();
    private final PartidaDAO partidaDAO = new PartidaDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/jogadores".equals(path)) {
                criar(exchange);

            }else if ("GET".equals(metodo) && path.matches("/jogadores/\\d+/partidas")) {
                String[] partes = path.split("/");
                Long jogadorId = Long.parseLong(partes[2]);
                listarPartidas(exchange, jogadorId); 
            
            }else if ("GET".equals(metodo) && path.startsWith("/jogadores/")) {
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

    private void listarPartidas(HttpExchange exchange, Long jogadorId) throws IOException {
        java.util.List<Partida> partidas = partidaDAO.listarPorJogador(jogadorId);

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < partidas.size(); i++) {
            Partida partida = partidas.get(i);

            if (i > 0) {
                json.append(", ");
            }

            json.append("{")
                .append("\"id\": ").append(partida.getId()).append(", ")
                .append("\"campeao_id\": ").append(partida.getCampeao().getId()).append(", ")
                .append("\"duracao_min\": ").append(partida.getDuracaoMin()).append(", ")
                .append("\"role\": \"").append(partida.getRole()).append("\", ")
                .append("\"abates\": ").append(partida.getAbates()).append(", ")
                .append("\"mortes\": ").append(partida.getMortes()).append(", ")
                .append("\"assistencias\": ").append(partida.getAssistencias()).append(", ")
                .append("\"resultado\": \"").append(partida.getResultado()).append("\", ")
                .append("\"tipo\": \"").append(partida.getTipo()).append("\", ")
                .append("\"data\": \"").append(partida.getData()).append("\"")
                .append("}");
        }

        json.append("]");

        enviarResposta(exchange, 200, json.toString());
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