package br.com.jean.lolapirest;

import com.sun.net.httpserver.HttpServer;

import br.com.jean.lolapirest.config.Database;
import br.com.jean.lolapirest.controller.CampeaoController;
import br.com.jean.lolapirest.controller.ItemController;
import br.com.jean.lolapirest.controller.JogadorController;
import br.com.jean.lolapirest.controller.PartidaController;
import br.com.jean.lolapirest.controller.RunaController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        try {
            Database.inicializar();
            System.out.println("Banco de dados inicializado com sucesso.");

            HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

            servidor.createContext("/jogadores", new JogadorController());
            servidor.createContext("/campeoes", new CampeaoController());
            servidor.createContext("/itens", new ItemController());
            servidor.createContext("/runas", new RunaController());
            servidor.createContext("/partidas", new PartidaController());

            servidor.start();

            System.out.println("API rodando em http://localhost:8080");

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(
                "Não foi possível inicializar a aplicação.", e
            );
        }
    }
}