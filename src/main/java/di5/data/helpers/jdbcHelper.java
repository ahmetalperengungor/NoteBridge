package di5.data.helpers;

import di5.helpers.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class jdbcHelper {

    static Connection connection = null;

    public static Connection getConnection() {
        // install postgresql driver
        try {
            Class.forName("org.postgresql.Driver");

            // TODO: make it take information from .env
            String host = "bronto.ewi.utwente.nl";
            String dbName = "dab_di23242b_227";
            String url = "jdbc:postgresql://" + host + ":5432/" + dbName + "?currentSchema=dab_di23242b_227";
            String username = "dab_di23242b_227";
            String password = "38PdI5Whbzd28Bfu";

            try {
                Logger.log("Connecting to " + url);
                Connection initialConnection = DriverManager.getConnection(url, username, password);
                Logger.log("Database connection created");
                connection = initialConnection;
            } catch (SQLException sqle) {
                Logger.error("Database connection could not be created");
                sqle.printStackTrace();
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
