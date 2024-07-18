package dataaccess;

import Models.AuthData;
import Models.UserData;

public interface AuthDAO_interface {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuthData(AuthData authToken) throws DataAccessException;
    void deleteAuth(UserData username) throws DataAccessException;
}
