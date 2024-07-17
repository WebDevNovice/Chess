package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;
import dataaccess.RamMemory.AuthDAO_RAM;
import dataaccess.RamMemory.GameDAO_RAM;
import dataaccess.RamMemory.UserDAO_RAM;
import dataaccess.UserDao_interface;

public class Services {
    UserDao_interface userDao;
    AuthDAO_interface authDao;
    GameDA0_interface gameDao;

    public Services() {
        try {
            this.userDao = new UserDAO_RAM();
            this.authDao = new AuthDAO_RAM();
            this.gameDao = new GameDAO_RAM();
        }catch (DataAccessException e){
            System.out.println(e.getMessage());
        }
    }

    public UserData createUser(UserData user) {
        return null;
    }

    public AuthData login(UserData user) throws DataAccessException {
        try{
            if (userDao.getUser(user)){
                return authDao.createAuth(user);
            }
            throw new DataAccessException("User not found");
        }

    }

    public UserData deleteUser(UserData user) {
        return null;
    }

    public AuthData createAuth() {
        return null;
    }

}
