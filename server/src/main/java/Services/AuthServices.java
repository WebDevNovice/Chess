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
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new DataAccessException("Username and password are required");
        }
        else {
            return authDao.createAuth(user);
        }
    }
}
