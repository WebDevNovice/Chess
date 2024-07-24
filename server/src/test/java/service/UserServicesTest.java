package service;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.rammemory.AuthDAO_RAM;
import dataaccess.rammemory.UserDAO_RAM;
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
    void setUp() throws DataAccessException {
        this.userDao = new UserDAO_RAM();
        this.authDAO = new AuthDAO_RAM();
        this.userServices = new UserServices(userDao, authDAO);
    }


    @Test
    void register() throws DataAccessException, BadRequestException {
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
    void registerDuplicateUsername() throws DataAccessException,BadRequestException {
        UserData userData = new UserData("New","User","e@gmail.com");
        userDao.getUserDatabase().add(userData);
        assertThrows(BadRequestException.class, () -> userServices.register(userData));
    }

    @Test
    void login() throws DataAccessException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        UserData test = new UserData(myName, myPassword, myEmail);
        String newName = "Jake";
        String newPassword = "12345";
        UserData test2 = new UserData(newName, newPassword,null);
        userDao.getUserDatabase().add(test);
        assertInstanceOf(AuthData.class, userServices.login(test2));
    }


    @Test
    void loginFailedPassword() throws DataAccessException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        UserData test = new UserData(myName, myPassword, myEmail);
        userDao.getUserDatabase().add(test);
        String wrongPassword = "wrongPassword";
        UserData test2 = new UserData(myName, wrongPassword, myEmail);
        assertThrows(DataAccessException.class, () -> userServices.login(test2));
    }

    @Test
    void loginFailedUsername() throws DataAccessException {
        String myName = "Jake";
        String myEmail = "jacobgbullock3@gmail.com";
        String myPassword = "12345";
        new UserData(myName, myPassword, myEmail);
        String wrong_username = "wrong_username";
        UserData test2 = new UserData(wrong_username, myPassword, myEmail);
        assertThrows(DataAccessException.class, () -> userServices.login(test2));
    }

}