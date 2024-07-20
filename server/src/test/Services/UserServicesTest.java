package Services;

import Models.UserData;
import dataaccess.DataAccessException;
import dataaccess.RamMemory.UserDAO_RAM;
import dataaccess.UserDao_interface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServicesTest {
    UserDao_interface userDao;
    UserServices userServices;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.userDao = new UserDAO_RAM();
        this.userServices = new UserServices(userDao);
    }


    @Test
    void register() throws DataAccessException {
        UserData userData = new UserData("New","User","e@gmail.com");
        UserData newUser = userServices.Register(userData);
        assertInstanceOf(UserData.class, newUser);
    }

    @Test
    void registerUsernameMissing() throws DataAccessException {
        UserData userData = new UserData(null,"User","e@gmail.com");
        assertThrows(DataAccessException.class, () -> userServices.Register(userData));
    }

    @Test
    void registerDuplicateUsername() throws DataAccessException {
        UserData userData = new UserData("New","User","e@gmail.com");
        UserData newUser = userServices.Register(userData);
        UserData duplicateUser = new UserData("New","User","e@gmail.com");
        assertThrows(DataAccessException.class, () -> userServices.Register(duplicateUser));
    }

    @Test
    void login() throws DataAccessException {
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        assertEquals(test, userServices.login(test));
    }

    @Test
    void loginFailedPassword() throws DataAccessException {
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "1234";
        UserData test = new UserData(my_name, my_password,my_email);
        assertThrows(DataAccessException.class, () -> userServices.login(test));
    }

    @Test
    void loginFailedUsername() throws DataAccessException {
        String my_name = "Jak";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        assertThrows(DataAccessException.class, () -> userServices.login(test));
    }

}