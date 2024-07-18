package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.RamMemory.AuthDAO_RAM;
import dataaccess.RamMemory.GameDAO_RAM;
import dataaccess.RamMemory.UserDAO_RAM;
import dataaccess.UserDao_interface;

public class UserServices {
    UserDao_interface userDao;
    AuthDAO_interface authDao;
    GameDA0_interface gameDao;

    public UserServices() {
        try {
            this.userDao = new UserDAO_RAM();
            this.authDao = new AuthDAO_RAM();
            this.gameDao = new GameDAO_RAM();
        }catch (DataAccessException e){
            System.out.println(e.getMessage());
        }
    }

    public AuthData Register(UserData user) throws DataAccessException {
        return userDao.createUser(user);
    }

    public AuthData login(UserData user) throws DataAccessException {
            UserData registeredUser = userDao.getUser(user);
            return authDao.createAuth(registeredUser);
    }

    public Object logout(AuthData authToken) throws DataAccessException {
        return authDao.deleteAuth(authToken);
    }

}
