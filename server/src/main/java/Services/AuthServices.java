package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;

public class AuthServices {
    AuthDAO_interface authDao;

    public AuthServices(AuthDAO_interface authDao) {
        this.authDao = authDao;
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
            return authDao.createAuth(user);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Authtoken Is Required");
        }
        else {
            return authDao.getAuthData(authToken);
        }
    }

    public Object logout(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("Auth object is required");
        }
        else {
            authDao.deleteAuth(authToken);
            return "";
        }
    }
}
