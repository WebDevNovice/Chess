package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.UserDao_interface;

public class UserServices {
    UserDao_interface userDao;
    AuthDAO_interface authDao;
    GameDA0_interface gameDao;

    public UserServices(UserDao_interface userDao, AuthDAO_interface authDao, GameDA0_interface gameDao) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
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
