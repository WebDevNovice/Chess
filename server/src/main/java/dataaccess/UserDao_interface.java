package dataaccess;

import Models.AuthData;
import Models.UserData;

public interface UserDao_interface{
    AuthData createUser(UserData userData) throws DataAccessException;
    boolean getUser() throws DataAccessException;
    void deleteUser(UserData userData) throws DataAccessException;
}
