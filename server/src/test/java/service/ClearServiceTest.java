package service;

import chess.ChessGame;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import dataaccess.UserDaoInterface;
import dataaccess.rammemory.GameDAORAM;
import dataaccess.rammemory.UserDAORAM;
import dataaccess.sqlMemory.AuthDAOSQL;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import dataaccess.sqlMemory.UserDAOSQL;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.serverservices.ClearService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    AuthDAOInterface authDao;
    UserDaoInterface userDao;
    GameDA0Interface gameDao;
    ClearService clearService;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.authDao = new AuthDAOSQL();
        this.userDao = new UserDAOSQL();
        this.gameDao = new GameDAOSQL();
        this.clearService = new ClearService(userDao, authDao, gameDao);
    }

    @Test
    void clearALlDatabasesSuccess() throws DataAccessException, ResponseException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        UserData userData = new UserData(username, uuid, email);

        authDao.createAuth(userData);
        userDao.createUser(userData);
        gameDao.createGame(gameName);

        clearService.clearAllDatabases();

        assertEquals(authDao.getAuthDatabase().isEmpty(), clearService.getAuthDao().getAuthDatabase().isEmpty());
        assertEquals(userDao.getUserDatabase().isEmpty(), clearService.getUserDao().getUserDatabase().isEmpty());
        assertEquals(gameDao.getGameDatabase().isEmpty(), clearService.getGameDao().getGameDatabase().isEmpty());
    }

    @Test
    void clearALlDatabasesFailure() throws DataAccessException, ResponseException {
        String username = "Joseph";
        String password = "Smith";
        String email = "joseph@gmail.com";
        String uuid = UUID.randomUUID().toString();
        String gameName = "Danites Duel";

        UserData userData = new UserData(username, uuid, email);
        GameData gameData = new GameData(null, null, gameName, new ChessGame(), 1);

        authDao.createAuth(userData);
        userDao.createUser(userData);
        gameDao.createGame(gameName);

        clearService.clearAllDatabases();

        userDao.createUser(new UserData("Jake","12345", "hello"));

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
        assertNotEquals(new UserDAORAM(), clearService.getUserDao());
    }

    @Test
    void getAuthDaoFailure() {
        assertInstanceOf(AuthDAOSQL.class, clearService.getAuthDao());
    }

    @Test
    void getGameDaoFailure() {
        assertNotEquals(new GameDAORAM(), clearService.getGameDao());
    }
}