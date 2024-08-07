package service.serverservices;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import service.execeptions.BadRequestException;
import spark.Request;

public class AuthServices {
    public AuthDAOInterface authDao;

    public AuthServices(AuthDAOInterface authDao) {
        this.authDao = authDao;
    }

    public AuthData createAuth(UserData user) throws DataAccessException, ResponseException {
        if (user.getUsername() == null || user.getPassword() == null
         || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new DataAccessException("Error: Username is required");
        }
            return authDao.createAuth(user);
    }

    public AuthData getAuth(String authToken) throws DataAccessException, BadRequestException, ResponseException {
        if (authToken == null) {
            throw new DataAccessException("Error: Authtoken Is Required");
        }
        else {
            return authDao.getAuthData(authToken);
        }
    }

    public Object logout(String authToken) throws DataAccessException, BadRequestException {
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("Error: Auth Token is required");
        }
        else {
            try {
                authDao.getAuthData(authToken);
                authDao.deleteAuth(authToken);
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public AuthData isAuthenticated(Request req) throws BadRequestException, DataAccessException, ResponseException {
        String authToken = req.headers("Authorization");
        return authDao.getAuthData(authToken);
    }
}
