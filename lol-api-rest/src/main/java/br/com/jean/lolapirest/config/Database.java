package br.com.jean.lolapirest.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    private static final String URL = "jdbc:h2:./data/lolhistorico";
    private static final String USER = "sa";
    private static final String PASSWORD = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}