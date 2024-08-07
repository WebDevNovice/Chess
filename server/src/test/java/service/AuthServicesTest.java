package service;

import dataaccess.sqlMemory.AuthDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;
import service.serverservices.AuthServices;

import static org.junit.jupiter.api.Assertions.*;

class AuthServicesTest {
    AuthDAOInterface authDoa;
    AuthServices authServices;

    @BeforeEach
    void setUp() throws ResponseException, DataAccessException {
        this.authDoa  = new AuthDAOSQL();
        this.authServices = new AuthServices(authDoa);
        authDoa.clearAuthDatabase();
    }

    @Test
    void createAuthSuccess() throws DataAccessException, ResponseException {
        UserData user = new UserData("Hello", "There", "jediKiller@darkside.org");
        AuthData newAuth = authServices.createAuth(user);
        assertInstanceOf(AuthData.class, newAuth);
    }

    @Test
    void createAuthFailedMissingUsername() throws DataAccessException {
        UserData user = new UserData("", "There", "jediKiller@darkside.org");
        assertThrows(DataAccessException.class, () -> authServices.createAuth(user));
    }

    @Test
    void createAuthFailedMissingPassword() throws DataAccessException {
        UserData user = new UserData("Hello", "", "jediKiller@darkside.org");
        assertThrows(DataAccessException.class, () -> authServices.createAuth(user));
    }

    @Test
    void getAuthSuccess() throws DataAccessException, BadRequestException, ResponseException {
        UserData user = new UserData("Hello", "Kenobi", "jediKiller@darkside.org");
        AuthData authData = authDoa.createAuth(user);
        assertEquals(authData.getAuthToken(), authDoa.getAuthData(authData.getAuthToken()).getAuthToken());
    }

    @Test
    void getAuthFailedWrongAuthToken() {
        assertThrows(DataAccessException.class, () -> authServices.getAuth("jediKiller"));
    }

    @Test
    void logout() throws DataAccessException, BadRequestException, ResponseException {
        UserData userData = new UserData("Hello", "There", null);
        AuthData authData = authDoa.createAuth(userData);
        assertNull(authServices.logout(authData.getAuthToken()));
    }

    @Test
    void logoutFailedWrongAuthToken() {
        assertThrows(DataAccessException.class, () -> authServices.logout("Ther"));
    }
}