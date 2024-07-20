package dataaccess.RamMemory;


import Models.AuthData;
import Models.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;

import java.util.ArrayList;
import java.util.Collection;


public class UserDAO_RAM implements UserDao_interface {

   Collection<UserData> userDatabase;

    public UserDAO_RAM() throws DataAccessException {
        this.userDatabase = new ArrayList<>();
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        for (UserData user : userDatabase) {

            if (!isUsernameInDatabase(userData, user)) {
                continue;
            }
            throw new DataAccessException("User " + userData.getUsername() + " already exists");
        }
        if (isUserDataComplete(userData)) {

            userDatabase.add(userData);
            return userData;

        }else {
                incompleteDataHandler(userData);
            }
        throw new DataAccessException("Data Entry Failure");
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException{
        for (UserData user : userDatabase) {

            if (userData.getUsername() == null) {
                throw new DataAccessException("Username is empty");
            }

            if (isUsernameInDatabase(userData, user)) {

                if (arePasswordsSame(user, userData)) {
                    return user;
                }
            }
        }
        throw new DataAccessException("User does not exist");
    }

    @Override
    public void clearUserDatabase(){
            userDatabase.clear();
    }

    public Collection<UserData> getUserDatabase() throws DataAccessException {
        return userDatabase;
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
            throw new DataAccessException("Username is empty");
        }
        else if (userData.getPassword() == null) {
            throw new DataAccessException("Password is empty");
        }
        else if (userData.getEmail() == null) {
            throw new DataAccessException("Email is empty");
        }
    }

    private boolean isUsernameInDatabase(UserData newUser, UserData storedUser) throws DataAccessException {
        if(newUser.getUsername()==null || newUser.getUsername().isEmpty()){
            throw new DataAccessException("Username Required");
        }
        return newUser.getUsername().equals(storedUser.getUsername());
    }

    private boolean arePasswordsSame(UserData storedUser, UserData newUser) throws DataAccessException {
        if (newUser.getPassword() == null) {
            throw new DataAccessException("Password is empty");
        }
        if (!storedUser.getPassword().equals(newUser.getPassword())) {
            throw new DataAccessException("Passwords do not match");
        }
        return true;
    }

}




