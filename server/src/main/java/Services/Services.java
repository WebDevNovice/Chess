package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.GameDA0_interface;
import dataaccess.UserDao_interface;

public class Services {
    UserDao_interface userDao;
    AuthDAO_interface authDao;
    GameDA0_interface gameDao;

    public Services(UserDao_interface userDao, AuthDAO_interface authDao, GameDA0_interface gameDao) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public UserData createUser(UserData user) {
        return null;
    }

    public AuthData getUser(UserData user) {
        if (userDao.getUser(user)!=null){
           AuthData authToken = createAuth();
           return authToken;
        }
        return null;
    }

    public UserData deleteUser(UserData user) {
        return null;
    }

    public AuthData createAuth() {
        return null;
    }

}
