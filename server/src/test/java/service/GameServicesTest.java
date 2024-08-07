package service;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import dataaccess.rammemory.AuthDAORAM;
import dataaccess.rammemory.GameDAORAM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import service.serverservices.GameServices;

import static org.junit.jupiter.api.Assertions.*;

class GameServicesTest {
    GameDA0Interface gameDao;
    AuthDAOInterface authDao;
    GameServices gameServices;

    @BeforeEach
    void setUp() {
        gameDao = new GameDAORAM();
        authDao = new AuthDAORAM();
        gameServices = new GameServices(gameDao);
    }

    @Test
    void listGames() throws DataAccessException, ResponseException {
        for (int i = 0; i < 3; i++) {
            String gameName = "Skyrim" + i;
            gameServices.createGame(gameName);
        }
        assertEquals(3, gameDao.listGames().size());
    }

    @Test
    void createGameSuccess() throws DataAccessException, ResponseException {
        String gameName = "Skyrim";
        Integer gameId = gameServices.createGame(gameName);
        assertEquals(1, gameId);
    }

    @Test
    void createGameFailureGameAlreadyExists() throws DataAccessException, ResponseException {
        String gameName = "Skyrim";
        gameServices.createGame(gameName);
        assertThrows(DataAccessException.class, () -> gameServices.createGame(gameName));
    }

    @Test
    void joinGameSuccess() throws DataAccessException, BadRequestException, UnvailableTeamException, ResponseException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "WHITE";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.createGame(gameName);
        GameData testGame = gameServices.joinGame(teamColor, 1, authData);
        assertEquals(gameDao.getGameDatabase().get(1), testGame);
    }

    @Test
    void joinGameFailureWHITEUserTaken() throws DataAccessException, BadRequestException, UnvailableTeamException, ResponseException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "WHITE";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.createGame(gameName);
        gameServices.joinGame(teamColor, 1, authData);
        UnvailableTeamException thrownException = assertThrows(UnvailableTeamException.class, () ->
                gameServices.joinGame(teamColor, 1, authData)
        );

        assertEquals("Error: OOPS! Someone has already taken that team Color", thrownException.getMessage());
    }

    @Test
    void joinGameFailureBLACKUserTaken() throws DataAccessException, BadRequestException, UnvailableTeamException, ResponseException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "BLACK";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.createGame(gameName);
        gameServices.joinGame(teamColor, 1, authData);
        UnvailableTeamException thrownException = assertThrows(UnvailableTeamException.class, () ->
                gameServices.joinGame(teamColor, 1, authData)
        );

        assertEquals("Error: OOPS! Someone has already taken that team Color", thrownException.getMessage());
    }

}