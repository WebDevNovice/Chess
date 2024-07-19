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
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        userDatabase.add(test);
    }


    @Override
    public AuthData createUser(UserData userData) throws DataAccessException {
        AuthData authData;
        for (UserData user : userDatabase) {

            if (!isUsernameInDatabase(userData, user)) {

                if (isUserDataComplete(userData)) {

                    userDatabase.add(userData);
                    authData = new AuthDAO_RAM().createAuth(userData);
                    return authData;
                }
                incompleteDataHandler(userData);
            }
            throw new DataAccessException("User " + userData.getUsername() + " already exists");
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
    public void clearUserDatabase() throws DataAccessException {
        if (!userDatabase.isEmpty()){
            userDatabase.clear();
        }
        else{
            throw new DataAccessException("Database is empty");
        }
    }

    public Collection<UserData> getAllUsers() throws DataAccessException {
        return userDatabase;
    }

    private boolean isUserDataComplete(UserData userData){
        if (userData.getUsername() == null || userData.getUsername().equals("")){
            return false;
        }
        if (userData.getPassword() == null || userData.getPassword().equals("")){
            return false;
        }
        if (userData.getEmail() == null || userData.getEmail().equals("")){
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

    private boolean isUsernameInDatabase(UserData newUser, UserData storedUser) {
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




