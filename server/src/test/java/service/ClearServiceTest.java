package service;

import chess.ChessGame;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import dataaccess.UserDaoInterface;
import dataaccess.rammemory.AuthDAO_RAM;
import dataaccess.rammemory.GameDAO_RAM;
import dataaccess.rammemory.UserDAO_RAM;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    AuthDAOInterface authDao;
    UserDaoInterface userDao;
    GameDA0Interface gameDao;
    ClearService clearService;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.authDao = new AuthDAO_RAM();
        this.userDao = new UserDAO_RAM();
        this.gameDao = new GameDAO_RAM();
        this.clearService = new ClearService(userDao, authDao, gameDao);
    }

    @Test
    void clearALlDatabasesSuccess() throws DataAccessException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        AuthData authData = new AuthData(username, password);
        UserData userData = new UserData(username, uuid, email);
        GameData gameData = new GameData(null, null, gameName, new ChessGame(), 1);

        authDao.getAuthDatabase().add(authData);
        userDao.getUserDatabase().add(userData);
        gameDao.getGameDatabase().put(gameData.getGameID(), gameData);

        clearService.clearAllDatabases();

        assertEquals(authDao.getAuthDatabase().isEmpty(), clearService.getAuthDao().getAuthDatabase().isEmpty());
        assertEquals(userDao.getUserDatabase().isEmpty(), clearService.getUserDao().getUserDatabase().isEmpty());
        assertEquals(gameDao.getGameDatabase().isEmpty(), clearService.getGameDao().getGameDatabase().isEmpty());
    }

    @Test
    void clearALlDatabasesFailure() throws DataAccessException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        AuthData authData = new AuthData(username, password);
        UserData userData = new UserData(username, uuid, email);
        GameData gameData = new GameData(null, null, gameName, new ChessGame(), 1);

        authDao.getAuthDatabase().add(authData);
        userDao.getUserDatabase().add(userData);
        gameDao.getGameDatabase().put(gameData.getGameID(), gameData);

        clearService.clearAllDatabases();

        userDao.getUserDatabase().add(new UserData("Jake","12345", "hello"));

        assertEquals(false, clearService.getUserDao().getUserDatabase().isEmpty());
    }

    @Test
    void getUserDaoSuccess() {
        assertEquals(userDao, clearService.getUserDao());
    }

    @Test
    void getAuthDaoSuccess() {
        assertEquals(authDao, clearService.getAuthDao());
    }

    @Test
    void getGameDaoSuccess() {
        assertEquals(gameDao, clearService.getGameDao());
    }

    @Test
    void getUserDaoFailure() throws DataAccessException {
        assertNotEquals(new UserDAO_RAM(), clearService.getUserDao());
    }

    @Test
    void getAuthDaoFailure() {
        assertInstanceOf(AuthDAO_RAM.class, clearService.getAuthDao());
    }

    @Test
    void getGameDaoFailure() {
        assertNotEquals(new GameDAO_RAM(), clearService.getGameDao());
    }
}