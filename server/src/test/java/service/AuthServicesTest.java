package service;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.rammemory.AuthDAO_RAM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class AuthServicesTest {
    AuthDAOInterface authDoa;
    AuthServices authServices;

    @BeforeEach
    void setUp() {
        this.authDoa  = new AuthDAO_RAM();
        this.authServices = new AuthServices(authDoa);
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
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
    void getAuthSuccess() throws DataAccessException, BadRequestException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        assertEquals(authData, authServices.getAuth("There"));
    }

    @Test
    void getAuthFailedWrongAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        assertThrows(DataAccessException.class, () -> authServices.getAuth("jediKiller"));
    }

    @Test
    void logout() throws DataAccessException, BadRequestException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        assertNull(authServices.logout("There"));
    }

    @Test
    void logoutFailedWrongAuthToken() throws DataAccessException, BadRequestException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        authServices.getAuth("There");
        assertThrows(DataAccessException.class, () -> authServices.logout("Ther"));
    }
}