package br.com.jean.lolapirest.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.h2.tools.RunScript;

public class Database{
    private static final String URL = "jdbc:h2:./data/lolhistorico";
    private static final String USER = "sa";
    private static final String PASSWORD = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void inicializar() throws SQLException, IOException {
    InputStream arquivo =
            Database.class.getResourceAsStream("/schema.sql");

    if (arquivo == null) {
        throw new IOException("Arquivo schema.sql não encontrado");
    }

    try (InputStreamReader leitor =
                 new InputStreamReader(arquivo, StandardCharsets.UTF_8);
         Connection conexao = getConnection()) {

        RunScript.execute(conexao, leitor);
    }
}
}