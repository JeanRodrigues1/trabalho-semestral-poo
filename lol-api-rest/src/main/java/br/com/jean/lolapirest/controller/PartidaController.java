package br.com.jean.lolapirest.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.jean.lolapirest.dao.CampeaoDAO;
import br.com.jean.lolapirest.dao.JogadorDAO;
import br.com.jean.lolapirest.dao.PartidaDAO;
import br.com.jean.lolapirest.dao.PartidaItemDAO;
import br.com.jean.lolapirest.dao.PartidaRunaDAO;
import br.com.jean.lolapirest.model.Campeao;
import br.com.jean.lolapirest.model.Item;
import br.com.jean.lolapirest.model.Jogador;
import br.com.jean.lolapirest.model.Partida;
import br.com.jean.lolapirest.model.PartidaCasual;
import br.com.jean.lolapirest.model.PartidaRanqueada;
import br.com.jean.lolapirest.model.Resultado;
import br.com.jean.lolapirest.model.Role;
import br.com.jean.lolapirest.model.Runa;
import br.com.jean.lolapirest.service.PartidaService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class PartidaController implements HttpHandler {

    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final JogadorDAO jogadorDAO = new JogadorDAO();
    private final CampeaoDAO campeaoDAO = new CampeaoDAO();
    private final PartidaItemDAO partidaItemDAO = new PartidaItemDAO();
    private final PartidaRunaDAO partidaRunaDAO = new PartidaRunaDAO();
    private final PartidaService partidaService = new PartidaService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equals(metodo) && "/partidas".equals(path)) {
                criar(exchange);

            } else if ("GET".equals(metodo) && path.startsWith("/partidas/")) {
                Long id = Long.parseLong(path.replace("/partidas/", ""));
                buscarPorId(exchange, id);

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
        partidaService.validarRunas(extrairValor(corpo, "runas"));
        partidaService.validarItens(extrairValor(corpo, "itens"));
        Jogador jogador = jogadorDAO.buscarPorId(Long.parseLong(extrairValor(corpo, "jogador_id")));
        Campeao campeao = campeaoDAO.buscarPorId(Long.parseLong(extrairValor(corpo, "campeao_id")));
        if (jogador == null || campeao == null) {
            enviarResposta(exchange, 400, "{\"erro\": \"Jogador ou campeão não encontrado\"}");
            return;
        }

        LocalDateTime data = LocalDateTime.now();
        int duracaoMin = Integer.parseInt(extrairValor(corpo, "duracao_min"));
        Role role = Role.valueOf(extrairValor(corpo, "role"));
        int abates = Integer.parseInt(extrairValor(corpo, "abates"));
        int mortes = Integer.parseInt(extrairValor(corpo, "mortes"));
        int assistencias = Integer.parseInt(extrairValor(corpo, "assistencias"));
        Resultado resultado = Resultado.valueOf(extrairValor(corpo, "resultado"));
        String tipo = extrairValor(corpo, "tipo");

        Partida partida;
        String[] idsRunas = extrairValor(corpo, "runas").split(",");
        if ("RANQUEADA".equals(tipo)) {
            int pdl = Integer.parseInt(extrairValor(corpo, "pdl_variacao"));
            partida = new PartidaRanqueada(jogador, campeao, data, duracaoMin, role,
                    abates, mortes, assistencias, resultado, pdl);
        } else if ("CASUAL".equals(tipo)) {
            partida = new PartidaCasual(jogador, campeao, data, duracaoMin, role,
                    abates, mortes, assistencias, resultado, extrairValor(corpo, "modo_jogo"));
        } else {
            enviarResposta(exchange, 400, "{\"erro\": \"tipo deve ser RANQUEADA ou CASUAL\"}");
            return;
        }        

        partidaDAO.inserirPartida(partida);

        for (String idItem : extrairValor(corpo, "itens").split(",")) {
            partidaItemDAO.inserirPartidaItem(partida.getId(), Long.parseLong(idItem.trim()));
        }

        for (String idRuna : idsRunas) {
            partidaRunaDAO.inserirPartidaRuna(partida.getId(), Long.parseLong(idRuna.trim()));
        }

        enviarResposta(exchange, 201, "{\"mensagem\": \"Partida criada\"}");
    }

    private void buscarPorId(HttpExchange exchange, Long id) throws IOException {
        Partida partida = partidaDAO.buscarPorId(id);

        if (partida == null) {
            enviarResposta(exchange, 404, "{\"erro\": \"Partida não encontrada\"}");
            return;
        }

        java.util.List<Item> itens = partidaItemDAO.listarItensPorPartida(id);
        java.util.List<Runa> runas = partidaRunaDAO.listarRunasPorPartida(id);

        StringBuilder itensJson = new StringBuilder("[");
        for (int i = 0; i < itens.size(); i++) {
            if (i > 0) itensJson.append(", ");
            Item item = itens.get(i);
            itensJson.append("{")
                .append("\"id\": ").append(item.getId()).append(", ")
                .append("\"nome\": \"").append(item.getNome()).append("\", ")
                .append("\"custo\": ").append(item.getCusto()).append(", ")
                .append("\"tipo_item\": \"").append(item.getTipo()).append("\"")
                .append("}");
        }
        itensJson.append("]");

        StringBuilder runasJson = new StringBuilder("[");
        for (int i = 0; i < runas.size(); i++) {
            if (i > 0) runasJson.append(", ");
            Runa runa = runas.get(i);
            runasJson.append("{")
                .append("\"id\": ").append(runa.getId()).append(", ")
                .append("\"nome\": \"").append(runa.getNome()).append("\", ")
                .append("\"caminho\": \"").append(runa.getCaminho()).append("\", ")
                .append("\"tipo_runa\": \"").append(runa.getTipo()).append("\"")
                .append("}");
        }
        runasJson.append("]");

        String json = "{"
            + "\"id\": " + partida.getId() + ", "
            + "\"jogador_id\": " + partida.getJogador().getId() + ", "
            + "\"campeao_id\": " + partida.getCampeao().getId() + ", "
            + "\"duracao_min\": " + partida.getDuracaoMin() + ", "
            + "\"role\": \"" + partida.getRole() + "\", "
            + "\"abates\": " + partida.getAbates() + ", "
            + "\"mortes\": " + partida.getMortes() + ", "
            + "\"assistencias\": " + partida.getAssistencias() + ", "
            + "\"resultado\": \"" + partida.getResultado() + "\", "
            + "\"data\": \"" + partida.getData() + "\""
            + "\"itens\": " + itensJson.toString() + ", "
            + "\"runas\": " + runasJson.toString()
            + "}";

        enviarResposta(exchange, 200, json);
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