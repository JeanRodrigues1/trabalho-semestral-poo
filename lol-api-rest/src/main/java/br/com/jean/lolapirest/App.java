package br.com.jean.lolapirest;

import java.io.IOException;
import java.sql.SQLException;

import br.com.jean.lolapirest.config.Database;

public class App {
    public static void main(String[] args) {
        try {
            Database.inicializar();
            System.out.println("Banco de dados inicializado com sucesso.");
        } catch (SQLException | IOException e) {
            throw new IllegalStateException(
                    "Não foi possível inicializar o banco de dados.", e);
        }
    }
}