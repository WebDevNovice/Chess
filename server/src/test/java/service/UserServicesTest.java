package service;

import dataaccess.sqlMemory.AuthDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import dataaccess.sqlMemory.UserDAOSQL;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class UserServicesTest {
    UserDaoInterface userDao;
    AuthDAOInterface authDAO;
    UserServices userServices;

    @BeforeEach
    void setUp() throws DataAccessException, ResponseException {
        this.userDao = new UserDAOSQL();
        this.authDAO = new AuthDAOSQL();
        this.userServices = new UserServices(userDao, authDAO);
        userDao.clearUserDatabase();
        authDAO.clearAuthDatabase();
    }


    @Test
    void register() throws DataAccessException, BadRequestException, ResponseException {
        UserData userData = new UserData("New","User","e@gmail.com");
        AuthData newUser = userServices.register(userData);
        assertInstanceOf(AuthData.class, newUser);
    }

    @Test
    void registerUsernameMissing() throws DataAccessException {
        UserData userData = new UserData(null,"User","e@gmail.com");
        assertThrows(DataAccessException.class, () -> userServices.register(userData));
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException, BadRequestException, ResponseException {
        UserData userData = new UserData("New","User","e@gmail.com");
        userDao.createUser(userData);
        assertThrows(BadRequestException.class, () -> userServices.register(userData));
    }

    @Test
    void login() throws DataAccessException, ResponseException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        UserData test = new UserData(myName, myPassword, myEmail);
        String newName = "Jake";
        String newPassword = "12345";
        UserData test2 = new UserData(newName, newPassword,null);
        userDao.createUser(test);
        assertInstanceOf(AuthData.class, userServices.login(test2));
    }


    @Test
    void loginFailedPassword() throws DataAccessException, ResponseException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        UserData test = new UserData(myName, myPassword, myEmail);
        userDao.createUser(test);
        String wrongPassword = "wrongPassword";
        UserData test2 = new UserData(myName, wrongPassword, myEmail);
        assertThrows(ResponseException.class, () -> userServices.login(test2));
    }

    @Test
    void loginFailedUsername() throws DataAccessException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        new UserData(myName, myPassword, myEmail);
        String wrongUsername = "wrong_username";
        UserData test2 = new UserData(wrongUsername, myPassword, myEmail);
        assertThrows(DataAccessException.class, () -> userServices.login(test2));
    }

}