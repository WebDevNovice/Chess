package java.passoff.server.service;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.ramMemory.AuthDAO_RAM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthServices;
import service.execeptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class AuthServicesTest {
    AuthDAO_interface authDoa;
    AuthServices authServices;

    @BeforeEach
    void setUp() {
        this.authDoa  = new AuthDAO_RAM();
        this.authServices = new AuthServices(authDoa);
    }

    @Test
    void createAuth_Success() throws DataAccessException {
        UserData user = new UserData("Hello", "There", "jediKiller@darkside.org");
        AuthData newAuth = authServices.createAuth(user);
        assertInstanceOf(AuthData.class, newAuth);
    }

    @Test
    void createAuth_Failed_MissingUsername() throws DataAccessException {
        UserData user = new UserData("", "There", "jediKiller@darkside.org");
        assertThrows(DataAccessException.class, () -> authServices.createAuth(user));
    }

    @Test
    void createAuth_Failed_MissingPassword() throws DataAccessException {
        UserData user = new UserData("Hello", "", "jediKiller@darkside.org");
        assertThrows(DataAccessException.class, () -> authServices.createAuth(user));
    }

    @Test
    void getAuth_Success() throws DataAccessException, BadRequestException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        assertEquals(authData, authServices.getAuth("There"));
    }

    @Test
    void getAuth_Failed_WrongAuthToken() throws DataAccessException {
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
    void logout_Failed_WrongAuthToken() throws DataAccessException, BadRequestException {
        AuthData authData = new AuthData("Hello", "There");
        authServices.authDao.getAuthDatabase().add(authData);
        authServices.getAuth("There");
        assertThrows(DataAccessException.class, () -> authServices.logout("Ther"));
    }
}