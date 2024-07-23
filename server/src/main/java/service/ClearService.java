package service;

import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.UserDao_interface;

public class ClearService {

    UserDao_interface userDao;
    AuthDAO_interface authDao;
    GameDA0_interface gameDao;

    public ClearService(UserDao_interface userDao, AuthDAO_interface authDao, GameDA0_interface gameDao){
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public void clearALlDatabases() throws DataAccessException {
        userDao.clearUserDatabase();
        authDao.clearAuthDatabase();
        gameDao.clearGamedatabase();
    }

    public UserDao_interface getUserDao() {
        return userDao;
    }
    public AuthDAO_interface getAuthDao() {
        return authDao;
    }

    public GameDA0_interface getGameDao() {
        return gameDao;
    }
}
