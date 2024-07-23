package service;

import model.AuthData;
import model.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.rammemory.AuthDAO_RAM;
import dataaccess.rammemory.UserDAO_RAM;
import dataaccess.UserDao_interface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.execeptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

class UserServicesTest {
    UserDao_interface userDao;
    AuthDAO_interface authDAO;
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
        AuthData newUser = userServices.Register(userData);
        assertInstanceOf(AuthData.class, newUser);
    }

    @Test
    void registerUsernameMissing() throws DataAccessException {
        UserData userData = new UserData(null,"User","e@gmail.com");
        assertThrows(DataAccessException.class, () -> userServices.Register(userData));
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException,BadRequestException {
        UserData userData = new UserData("New","User","e@gmail.com");
        userDao.getUserDatabase().add(userData);
        assertThrows(BadRequestException.class, () -> userServices.Register(userData));
    }

    @Test
    void login() throws DataAccessException {
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        String newName = "Jake";
        String newPassword = "12345";
        UserData test2 = new UserData(newName, newPassword,null);
        userDao.getUserDatabase().add(test);
        assertInstanceOf(AuthData.class, userServices.login(test2));
    }


    @Test
    void loginFailedPassword() throws DataAccessException {
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        userDao.getUserDatabase().add(test);
        String wrong_password = "wrong_password";
        UserData test2 = new UserData(my_name, wrong_password,my_email);
        assertThrows(DataAccessException.class, () -> userServices.login(test2));
    }

    @Test
    void loginFailedUsername() throws DataAccessException {
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        String wrong_username = "wrong_username";
        UserData test2 = new UserData(wrong_username, my_password,my_email);
        assertThrows(DataAccessException.class, () -> userServices.login(test2));
    }

}