package dataaccess;

import dataaccess.sqlMemory.ResponseException;
import model.UserData;

import java.util.Collection;

public interface UserDaoInterface {
    UserData createUser(UserData userData) throws DataAccessException, ResponseException;
    UserData getUser(UserData user) throws DataAccessException, ResponseException;
    void clearUserDatabase() throws ResponseException, DataAccessException;
    Collection<UserData> getUserDatabase() throws DataAccessException, ResponseException;
}
