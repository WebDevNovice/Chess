package dataaccess.RamMemory;

import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAO_RAMTest{


    AuthDAO_RAMTest() throws DataAccessException {
    }

    @Test
    void createAuth_databaseEmpty() throws DataAccessException {
        UserData userData = new UserData("John", "The", "Beast@Feastables.com");
        AuthDAO_RAM authDAORam = new AuthDAO_RAM();
        AuthData authToken = authDAORam.createAuth(userData);
        assertNotNull(authToken);
    }

    @Test
    void createAuth_databaseNotEmpty() throws DataAccessException {
        UserData userData1 = new UserData("John", "The", "Beast@Feastables.com");
        UserData userData2 = new UserData("Jimmy", "The", "Beast@Feastables.com");
        UserData userData3 = new UserData("James", "The", "Beast@Feastables.com");

        AuthDAO_RAM authDAORam = new AuthDAO_RAM();

        AuthData authToken1 = authDAORam.createAuth(userData1);
        AuthData authToken2 = authDAORam.createAuth(userData2);
        AuthData authToken3 = authDAORam.createAuth(userData3);

        assertNotNull(authToken3);
    }

    @Test
    void authtokenAlreadyExist() throws DataAccessException {

        UserData userData1 = new UserData("John", "The", "Beast@Feastables.com");
        UserData userData2 = new UserData("Jimmy", "The", "Beast@Feastables.com");
        UserData userData3 = new UserData("John", "The", "");

        AuthDAO_RAM authDAORam = new AuthDAO_RAM();

        AuthData authToken1 = authDAORam.createAuth(userData1);
        AuthData authToken2 = authDAORam.createAuth(userData2);

        Assertions.assertThrows(DataAccessException.class, () -> authDAORam.createAuth(userData3));
    }

    @Test
    void getAuthData() {
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void clearAuthDatabase() throws DataAccessException {
        Collection<UserData> empty = new ArrayList<>();

        UserData userData1 = new UserData("John", "The", "Beast@Feastables.com");
        UserData userData2 = new UserData("Jimmy", "The", "Beast@Feastables.com");
        UserData userData3 = new UserData("James", "The", "Beast@Feastables.com");

        AuthDAO_RAM authDAORam = new AuthDAO_RAM();

        AuthData authToken1 = authDAORam.createAuth(userData1);
        AuthData authToken2 = authDAORam.createAuth(userData2);
        AuthData authToken3 = authDAORam.createAuth(userData3);

        authDAORam.clearAuthDatabase();

        assertEquals(empty, authDAORam.authDatabase);
    }
}