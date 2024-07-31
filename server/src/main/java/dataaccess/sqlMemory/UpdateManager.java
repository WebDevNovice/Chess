package dataaccess.sqlMemory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UpdateManager {

    static int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                verifyParams(ps, params);

                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    static List<List<Object>> executeQuery(String statement, Object... params) throws DataAccessException, ResponseException {
        List<List<Object>> queryResult = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                verifyParams(ps, params);

                try (var rs = ps.executeQuery()) {
                    var metaData = rs.getMetaData();
                    var colcount = metaData.getColumnCount();

                    while (rs.next()) {
                        var row = new ArrayList<>();
                        for (var i = 1; i <= colcount; i++) {
                            row.add(rs.getObject(i));
                        }
                        queryResult.add(row);
                    }
                }
                return queryResult;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    static void verifyParams (PreparedStatement ps, Object... params) throws SQLException {
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            switch (param) {
                case String p -> ps.setString(i + 1, p);
                case Integer p -> ps.setInt(i + 1, p);
                case UserData p -> ps.setString(i + 1, p.toString());
                case AuthData p -> ps.setString(i + 1, p.toString());
                case ChessGame p -> ps.setString(i + 1, p.toString());
                case GameData p -> ps.setString(i + 1, p.toString());
                case null -> ps.setNull(i + 1, NULL);
                default -> {
                }
            }
        }
    }
}
