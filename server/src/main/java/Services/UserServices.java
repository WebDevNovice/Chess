package Services;

import Models.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;

public class UserServices {
    UserDao_interface userDao;


    public UserServices(UserDao_interface userDao) {
        this.userDao = userDao;

    }

    public UserData Register(UserData user) throws DataAccessException {
        return userDao.createUser(user);
    }

    public UserData login(UserData user) throws DataAccessException {
            return userDao.getUser(user);
    }


}
