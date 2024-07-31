package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
        executeQuery(statement);
    }

    static void createUserTable() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS user (" +
                                            "username VARCHAR(255) NOT NULL UNIQUE, " +
                                            "password VARCHAR(255) NOT NULL, " +
                                            "email varchar(255)) NOT NULL" +
                                            "PRIMARY KEY (username);)";

        executeQuery(statement);
    }

    static void createAuthTable() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS auth (" +
                "username VARCHAR(255) NOT NULL UNIQUE, " +
                "uuid VARCHAR(255) NOT NULL UNIQUE, " +
                "PRIMARY KEY (username);)";

        executeQuery(statement);
    }

    static void createGameTable() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS game (" +
                "id INT NOT NULL UNIQUE AUTO_INCREMENT, " +
                "white_player VARCHAR(255), " +
                "black_player VARCHAR(255), " +
                "game_name VARCHAR(255) NOT NULL, " +
                "game_data LONGTEXT NOT NULL, " +
                "PRIMARY KEY (id);)";

        executeQuery(statement);
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void executeQuery(String statement) throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
