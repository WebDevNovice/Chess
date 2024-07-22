package Services;

import Models.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;

public class UserServices {
    UserDao_interface userDao;

    //Follow the phase 2 diagram better
    public UserServices(UserDao_interface userDao) {
        this.userDao = userDao;

    }

    public UserData Register(UserData user) throws DataAccessException {
        UserData newUser =  userDao.createUser(user);
        for (UserData registeredUser : userDao.getUserDatabase()) {

            if (!isUsernameInDatabase(registeredUser, user)) {
                continue;
            }
            throw new DataAccessException("Error: User " + registeredUser.getUsername() + " already exists");
        }
    }

    public UserData login(UserData user) throws DataAccessException {
            return userDao.getUser(user);
    }

    private void incompleteDataHandler(UserData userData) throws DataAccessException {
        if (userData.getUsername() == null) {
            throw new DataAccessException("Error: Username is empty");
        }
        else if (userData.getPassword() == null) {
            throw new DataAccessException("Error: Password is empty");
        }
        else if (userData.getEmail() == null) {
            throw new DataAccessException("Error: Email is empty");
        }
    }

    private boolean isUsernameInDatabase(UserData newUser, UserData storedUser) throws DataAccessException {
        if(newUser.getUsername()==null || newUser.getUsername().isEmpty()){
            throw new DataAccessException("Error: Username Required");
        }
        return newUser.getUsername().equals(storedUser.getUsername());
    }

    private boolean arePasswordsSame(UserData storedUser, UserData newUser) throws DataAccessException {
        if (newUser.getPassword() == null) {
            throw new DataAccessException("Error: Password is empty");
        }
        if (!storedUser.getPassword().equals(newUser.getPassword())) {
            throw new DataAccessException("Error: Passwords do not match");
        }
        return true;
    }

}
