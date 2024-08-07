package service.serverservices;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import dataaccess.UserDaoInterface;
import dataaccess.sqlMemory.ResponseException;

public class ClearService {

    UserDaoInterface userDao;
    AuthDAOInterface authDao;
    GameDA0Interface gameDao;

    public ClearService(UserDaoInterface userDao, AuthDAOInterface authDao, GameDA0Interface gameDao){
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public void clearAllDatabases() throws DataAccessException, ResponseException {
        userDao.clearUserDatabase();
        authDao.clearAuthDatabase();
        gameDao.clearGamedatabase();
    }

    public UserDaoInterface getUserDao() {
        return userDao;
    }

    public AuthDAOInterface getAuthDao() {
        return authDao;
    }

    public GameDA0Interface getGameDao() {
        return gameDao;
    }
}
