package dataaccess;

import model.AuthData;
import model.UserData;
import service.execeptions.BadRequestException;

import java.util.Collection;

public interface AuthDAOInterface {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuthData(String authToken) throws DataAccessException, BadRequestException;
    void deleteAuth(String authToken) throws DataAccessException, BadRequestException;
    void clearAuthDatabase();
    Collection<AuthData> getAuthDatabase() throws DataAccessException;
}
