package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.serverservices.AuthServices;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOSQLTest {

    AuthDAOSQL authDAOSQL;
    UserDAOSQL userDAOSQL;
    UserData userData;
    AuthServices authServices;

    @BeforeEach
    void setUp() throws ResponseException, DataAccessException {
        authDAOSQL = new AuthDAOSQL();
        userDAOSQL = new UserDAOSQL();
        userData = new UserData("Billy","Bullock", "alliteration@email.com");
        authServices = new AuthServices(authDAOSQL);
        authDAOSQL.clearAuthDatabase();
    }

    @Test
    void createAuthSuccess() throws ResponseException, DataAccessException {
        AuthData newAuthData = authDAOSQL.createAuth(userData);
        assertEquals(newAuthData.getUsername(), userData.getUsername());
    }

    @Test
    void getAuthDataSuccess() throws ResponseException, DataAccessException {
        AuthData newAuthData = authDAOSQL.createAuth(userData);
        AuthData newAuth = authDAOSQL.getAuthData(newAuthData.getAuthToken());
        assertEquals(newAuthData.getAuthToken(), newAuth.getAuthToken());
    }

    @Test
    void getAuthDataFail() {
        assertThrows(DataAccessException.class, () -> authDAOSQL.getAuthData(null));
    }

    @Test
    void deleteAuthSuccess() throws ResponseException, DataAccessException {
        AuthData authData = authDAOSQL.createAuth(userData);
        authDAOSQL.deleteAuth(authData.getAuthToken());
        assertThrows(DataAccessException.class, () -> authDAOSQL.getAuthData(authData.getAuthToken()));
    }

    @Test
    void deleteAuthFail() throws ResponseException, DataAccessException {
        AuthData authData = authDAOSQL.createAuth(userData);
        authDAOSQL.deleteAuth(authData.getAuthToken());
        assertThrows(DataAccessException.class, () -> authDAOSQL.deleteAuth(null));
    }

    @Test
    void clearAuthDatabaseSuccess() throws ResponseException, DataAccessException {
        AuthData authData = authDAOSQL.createAuth(userData);
        authDAOSQL.clearAuthDatabase();
        assertThrows(DataAccessException.class, () -> authDAOSQL.getAuthData(authData.getAuthToken()));
    }

    @Test
    void clearAuthDatabaseFail() throws ResponseException, DataAccessException {
        AuthData authData = authDAOSQL.createAuth(userData);
        authDAOSQL.clearAuthDatabase();
        assertEquals(new ArrayList<>(), authDAOSQL.getAuthDatabase());
    }

}