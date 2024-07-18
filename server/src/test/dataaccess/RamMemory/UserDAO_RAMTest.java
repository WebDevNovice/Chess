package dataaccess.RamMemory;

import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDAO_RAMTest {

    private static AuthDAO_RAM authDAO_RAM;
    private UserDAO_RAM userDAORam;

    @BeforeAll
    static void setupAll() {
        authDAO_RAM = new AuthDAO_RAM();
    }

    @BeforeEach
    void setup() throws DataAccessException {
        userDAORam = new UserDAO_RAM();
    }

    @Test
    void createUser_ReturnsCorrectUsername() throws DataAccessException {
        UserData user = new UserData("Jacob", "123456", "pglinebacker20@gmail.com");
        AuthData authData = authDAO_RAM.createAuth(user);
        String username = userDAORam.createUser(user).getUsername();
        Assertions.assertEquals(authData.getUsername(), username);
    }

    @Test
    void createUser_ThrowsIncompleteUserError() throws DataAccessException {
        UserData user = new UserData("Jacob", "123456", "");
        Assertions.assertThrows(DataAccessException.class, () -> userDAORam.createUser(user));
    }

    @Test
    void createUser_ThrowsUsernameAlreadyExistError() throws DataAccessException {
        UserData user = new UserData("Jake", "123456", "pglinebacker20@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userDAORam.createUser(user));
    }

    @Test
    void getUser() throws DataAccessException {
        UserData test = new UserData("Jake", "12345", null);
        UserData correct = new UserData("Jake", "12345", "jacobgbullock3@gmail.com");
        Assertions.assertEquals(correct, userDAORam.getUser(test));
    }

    @Test
    void getUser_ThrowsUsernameNotFoundError() throws DataAccessException {
        UserData test = new UserData("Billy", "12345", null);
        Assertions.assertThrows(DataAccessException.class, () -> userDAORam.getUser(test));
    }

    @Test
    void clear() throws DataAccessException {
        Collection<UserData> empty = new ArrayList<>();
        userDAORam.clearUserDatabase();
        assertEquals(empty, userDAORam.getAllUsers());
    }
}
