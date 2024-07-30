package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserServices;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOSQLTest {
    UserData userData;
    UserServices userServices;
    UserDAOSQL userDAOSQL;
    AuthDAOSQL authDAOSQL;

    @BeforeEach
    void setUp() throws ResponseException, DataAccessException {
        userData = new UserData("Billy", "Bullock", "jb@famous.com");
        userDAOSQL = new UserDAOSQL();
        authDAOSQL = new AuthDAOSQL();
        userServices = new UserServices(userDAOSQL, authDAOSQL);
        userDAOSQL.clearUserDatabase();
    }

    @Test
    void createUserSuccess() throws DataAccessException, ResponseException {
        UserData registeredUser = userDAOSQL.createUser(userData);
        assertEquals(userData.getUsername(), registeredUser.getUsername());
    }

    @Test
    void createUserFail() throws ResponseException, DataAccessException {
        assertThrows(ResponseException.class, () -> userDAOSQL.createUser(userData));
    }

    @Test
    void getUserSucess() throws ResponseException, DataAccessException {
        userDAOSQL.createUser(userData);
        UserData registeredUser = userDAOSQL.getUser(userData);
        assertEquals(userData.getUsername(), registeredUser.getUsername());
    }

    @Test
    void clearUserDatabaseSuccess() throws ResponseException, DataAccessException {
        userDAOSQL.clearUserDatabase();
        assertThrows(DataAccessException.class, ()-> userDAOSQL.getUser(userData));
    }
}