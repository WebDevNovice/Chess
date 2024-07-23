package java.passoff.server.service;

import chess.ChessGame;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.UserDao_interface;
import dataaccess.ramMemory.AuthDAO_RAM;
import dataaccess.ramMemory.GameDAO_RAM;
import dataaccess.ramMemory.UserDAO_RAM;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    AuthDAO_interface authDao;
    UserDao_interface userDao;
    GameDA0_interface gameDao;
    ClearService clearService;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.authDao = new AuthDAO_RAM();
        this.userDao = new UserDAO_RAM();
        this.gameDao = new GameDAO_RAM();
        this.clearService = new ClearService(userDao, authDao, gameDao);
    }

    @Test
    void clearALlDatabases_Success() throws DataAccessException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        AuthData authData = new AuthData(username, uuid);
        UserData userData = new UserData(username, uuid, email);
        GameData gameData = new GameData(null, null, gameName, new ChessGame(), 1);

        authDao.getAuthDatabase().add(authData);
        userDao.getUserDatabase().add(userData);
        gameDao.getGameDatabase().put(gameData.getGameID(), gameData);

        clearService.clearALlDatabases();

        assertEquals(authDao.getAuthDatabase().isEmpty(), clearService.getAuthDao().getAuthDatabase().isEmpty());
        assertEquals(userDao.getUserDatabase().isEmpty(), clearService.getUserDao().getUserDatabase().isEmpty());
        assertEquals(gameDao.getGameDatabase().isEmpty(), clearService.getGameDao().getGameDatabase().isEmpty());
    }

    @Test
    void clearALlDatabases_Failure() throws DataAccessException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        AuthData authData = new AuthData(username, uuid);
        UserData userData = new UserData(username, uuid, email);
        GameData gameData = new GameData(null, null, gameName, new ChessGame(), 1);

        authDao.getAuthDatabase().add(authData);
        userDao.getUserDatabase().add(userData);
        gameDao.getGameDatabase().put(gameData.getGameID(), gameData);

        clearService.clearALlDatabases();

        userDao.getUserDatabase().add(new UserData("Jake","12345", "hello"));

        assertEquals(false, clearService.getUserDao().getUserDatabase().isEmpty());
    }

    @Test
    void getUserDao_Success() {
        assertEquals(userDao, clearService.getUserDao());
    }

    @Test
    void getAuthDao_Success() {
        assertEquals(authDao, clearService.getAuthDao());
    }

    @Test
    void getGameDao_Success() {
        assertEquals(gameDao, clearService.getGameDao());
    }

    @Test
    void getUserDao_Failure() throws DataAccessException {
        assertNotEquals(new UserDAO_RAM(), clearService.getUserDao());
    }

    @Test
    void getAuthDao_Failure() {
        assertInstanceOf(AuthDAO_RAM.class, clearService.getAuthDao());
    }

    @Test
    void getGameDao_Failure() {
        assertNotEquals(new GameDAO_RAM(), clearService.getGameDao());
    }
}