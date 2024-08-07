package service.serverservices;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.UserData;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import service.execeptions.BadRequestException;

public class UserServices {
    UserDaoInterface userDao;
    AuthDAOInterface authDao;

    //Follow the phase 2 diagram better
    public UserServices(UserDaoInterface userDao, AuthDAOInterface authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData register(UserData user) throws DataAccessException, BadRequestException, ResponseException {

        if (isUserDataComplete(user)){
           if (!userDao.getUserDatabase().isEmpty()){
               for (UserData registeredUser : userDao.getUserDatabase()) {
                   //This next check should fail
                   if (!isUsernameInDatabase(registeredUser, user)) {
                       UserData newUser =  userDao.createUser(user);
                       return authDao.createAuth(newUser);
                   }
                   throw new BadRequestException("Error: User " + registeredUser.getUsername() + " already exists");
               }
           }
            UserData newUser =  userDao.createUser(user);
            return authDao.createAuth(newUser);
        }
        //If I have reached this point, one of my exceptions will be thrown in the next method call
        incompleteDataHandler(user);
        return null;
    }

    public AuthData login(UserData userData) throws DataAccessException, ResponseException {

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
