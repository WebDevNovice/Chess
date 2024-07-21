package Services;

import Models.AuthData;
import Models.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.RamMemory.GameDAO_RAM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServicesTest {
    GameDA0_interface gameDao;
    GameServices gameServices;

    @BeforeEach
    void setUp() {
        gameDao = new GameDAO_RAM();
        gameServices = new GameServices(gameDao);
    }

    @Test
    void listGames() throws DataAccessException {
        for (int i = 0; i < 3; i++) {
            String gameName = "Skyrim" + i;
            gameServices.CreateGame(gameName);
        }
        assertEquals(3, gameDao.listGames().size());
    }

    @Test
    void createGame_Success() throws DataAccessException {
        String gameName = "Skyrim";
        Integer gameId = gameServices.CreateGame(gameName);
        assertEquals(1, gameId);
    }

    @Test
    void createGame_Failure_GameAlreadyExists() throws DataAccessException {
        String gameName = "Skyrim";
        gameServices.CreateGame(gameName);
        assertThrows(DataAccessException.class, () -> gameServices.CreateGame(gameName));
    }

    @Test
    void joinGame_Success() throws DataAccessException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "WHITE";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.CreateGame(gameName);
        GameData testGame = gameServices.joinGame(teamColor, 1, authData);
        assertEquals(gameDao.getGameDatabase().get(1), testGame);
    }

    @Test
    void joinGame_Failure_WHITEUserTaken() throws DataAccessException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "WHITE";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.CreateGame(gameName);
        gameServices.joinGame(teamColor, 1, authData);
        DataAccessException thrownException = assertThrows(DataAccessException.class, () ->
                gameServices.joinGame(teamColor, 1, authData)
        );

        assertEquals("OOPS! Someone has already taken that team Color", thrownException.getMessage());
    }

    @Test
    void joinGame_Failure_BLACKUserTaken() throws DataAccessException {
        String gameName = "Skyrim";
        String playerName = "Jake";
        String teamColor = "BLACK";
        AuthData authData = new AuthData(playerName, teamColor);
        gameServices.CreateGame(gameName);
        gameServices.joinGame(teamColor, 1, authData);
        DataAccessException thrownException = assertThrows(DataAccessException.class, () ->
                gameServices.joinGame(teamColor, 1, authData)
        );

        assertEquals("OOPS! Someone has already taken that team Color", thrownException.getMessage());
    }

}