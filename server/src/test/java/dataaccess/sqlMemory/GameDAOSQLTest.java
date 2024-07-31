package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameDAOSQLTest {
    GameDAOSQL gameDAOSQL;
    AuthDAOSQL authDAOSQL;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException {
        gameDAOSQL = new GameDAOSQL();
        authDAOSQL = new AuthDAOSQL();
        authDAOSQL.clearAuthDatabase();
        gameDAOSQL.clearGamedatabase();
    }

    @Test
    void createGameSuccess() throws ResponseException, DataAccessException {
        String gameName = "testGame";
        Integer gameID = gameDAOSQL.createGame(gameName);
        var statement = "Select id from game where game_name = ?";
        List<List<Object>> id_list = UpdateManager.executeQuery(statement, gameName);
        Integer id = (Integer) id_list.getFirst().getFirst();
        assertEquals(id ,gameID);
    }

    @Test
    void createGameFailure() throws ResponseException, DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAOSQL.createGame(null));
    }

    @Test
    void listGamesSuccess() throws ResponseException, DataAccessException {
        gameDAOSQL.createGame("Test1");
        gameDAOSQL.createGame("Test2");
        gameDAOSQL.createGame("Test3");

        assertInstanceOf(Collection.class, gameDAOSQL.listGames());
    }

    @Test
    void joinGame() {
    }
}