package dataaccess;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.UserData;
import service.execeptions.BadRequestException;

import java.util.Collection;

public interface AuthDAOInterface {
    AuthData createAuth(UserData user) throws DataAccessException, ResponseException;
    AuthData getAuthData(String authToken) throws DataAccessException, BadRequestException, ResponseException;
    void deleteAuth(String authToken) throws DataAccessException, ResponseException;
    void clearAuthDatabase() throws ResponseException, DataAccessException;
    Collection<AuthData> getAuthDatabase() throws DataAccessException, ResponseException;
}
