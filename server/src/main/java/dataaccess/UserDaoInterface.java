package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDaoInterface {
    UserData createUser(UserData userData) throws DataAccessException;
    UserData getUser(UserData user) throws DataAccessException;
    void clearUserDatabase();
    Collection<UserData> getUserDatabase() throws DataAccessException;
}
