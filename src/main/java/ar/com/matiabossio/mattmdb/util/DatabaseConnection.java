package ar.com.matiabossio.mattmdb.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

// Ya no las uso porque estoy usando variables de entorno con la libreria dotenv-java:
// https://github.com/cdimascio/dotenv-java
/*
private static final String url = "jdbc:mysql://localhost:3306/matmdb";
private static final String username = "root";
private static final String password = "";
*/
public static Connection connection;

    public static Connection getDatabaseConnection() {

        // obtengo las variables de entorno:
        Dotenv dotenv = Dotenv.load();

        // las guardo en los atributos del enum:
        String URL = dotenv.get("URL");
        String USERNAME = dotenv.get("DBUSER");
        String PASSWORD = dotenv.get("DBPASSWORD");

        if(connection == null){
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println( "USER: " + USERNAME + " JUST CONNECTED TO: " + URL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println( "USER: " + USERNAME + " ALREADY CONNECTED TO: " + URL);

        return connection;
    }


}
