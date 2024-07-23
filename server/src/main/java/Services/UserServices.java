package Services;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;

public class UserServices {
    UserDao_interface userDao;
    AuthDAO_interface authDao;

    //Follow the phase 2 diagram better
    public UserServices(UserDao_interface userDao, AuthDAO_interface authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData Register(UserData user) throws DataAccessException {

        if (isUserDataComplete(user)){
           if (!userDao.getUserDatabase().isEmpty()){
               for (UserData registeredUser : userDao.getUserDatabase()) {
                   //This next check should fail
                   if (!isUsernameInDatabase(registeredUser, user)) {
                       UserData newUser =  userDao.createUser(user);
                       return authDao.createAuth(newUser);
                   }
                   throw new DataAccessException("Error: User " + registeredUser.getUsername() + " already exists");
               }
           }
            UserData newUser =  userDao.createUser(user);
            return authDao.createAuth(newUser);
        }
        //If I have reached this point, one of my exceptions will be thrown in the next method call
        incompleteDataHandler(user);
        return null;
    }

    public AuthData login(UserData userData) throws DataAccessException {

        UserData registeredUser = userDao.getUser(userData);
        if (registeredUser != null) {
            return authDao.createAuth(registeredUser);
        }
        throw new DataAccessException("Error: User does not exist");
    }

    private boolean isUserDataComplete(UserData userData){
        if (userData.getUsername() == null || userData.getUsername().isEmpty()){
            return false;
        }
        if (userData.getPassword() == null || userData.getPassword().isEmpty()){
            return false;
        }
        if (userData.getEmail() == null || userData.getEmail().isEmpty()){
            return false;
        }
        return true;
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
        if (newUser.getUsername().equals(storedUser.getUsername())){
            return true;
        }
        return false;
    }

}
