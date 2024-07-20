package dataaccess;

import Models.AuthData;
import Models.UserData;

import java.util.Collection;

public interface UserDao_interface{
    AuthData createUser(UserData userData) throws DataAccessException;
    UserData getUser(UserData user) throws DataAccessException;
    void clearUserDatabase() throws DataAccessException;
    Collection<UserData> getUserDatabase() throws DataAccessException;
}
