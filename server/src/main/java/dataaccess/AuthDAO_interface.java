package dataaccess;

import Models.AuthData;
import Models.UserData;

import java.util.Collection;

public interface AuthDAO_interface {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuthData(String authToken) throws DataAccessException;
    Object deleteAuth(String authToken) throws DataAccessException;
    void clearAuthDatabase();
    Collection<AuthData> getAuthDatabase() throws DataAccessException;
}
