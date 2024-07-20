package dataaccess;

import Models.AuthData;
import Models.UserData;

import java.util.Collection;

public interface AuthDAO_interface {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuthData(AuthData authToken) throws DataAccessException;
    Object deleteAuth(AuthData authData) throws DataAccessException;
    void clearAuthDatabase() throws DataAccessException;
    Collection<AuthData> getAuthDatabase() throws DataAccessException;
}
