package dataaccess.sqlMemory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.util.ArrayList;
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
    void listGamesFailure() throws ResponseException, DataAccessException {
        assertEquals(new ArrayList<>(), gameDAOSQL.listGames());
    }

    @Test
    void joinGameSuccess() throws ResponseException, DataAccessException, UnvailableTeamException, BadRequestException {
        Integer id = gameDAOSQL.createGame("Test1");
        AuthData authData = new AuthData("Jake",":)");
        String playerColor = ChessGame.TeamColor.BLACK.toString();
        GameData gameData = gameDAOSQL.joinGame(playerColor, id, authData);
        assertEquals(authData.getUsername(), gameData.getBlackUsername());
    }

    @Test
    void joinGameFailure() throws ResponseException, DataAccessException, UnvailableTeamException, BadRequestException {
        Integer id = gameDAOSQL.createGame("Test1");
        AuthData authData = new AuthData("Jake",":)");
        String playerColor = ChessGame.TeamColor.BLACK.toString();
        GameData gameData = gameDAOSQL.joinGame(playerColor, id, authData);
        assertThrows(UnvailableTeamException.class, () -> gameDAOSQL.joinGame(playerColor, id, authData));
    }

}