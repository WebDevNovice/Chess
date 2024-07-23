package dataaccess.ramMemory;


import model.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;

import java.util.ArrayList;
import java.util.Collection;


public class UserDAO_RAM implements UserDao_interface {

   public Collection<UserData> userDatabase;

    public UserDAO_RAM() throws DataAccessException {
        this.userDatabase = new ArrayList<>();
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
            userDatabase.add(userData);
            return userData;
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException{
        for (UserData user : userDatabase) {
            if(user.getUsername().equals(userData.getUsername()) &&
               user.getPassword().equals(userData.getPassword())){
                return user;
            }
        }
        throw new DataAccessException("Error: User Not Found");
    }

    @Override
    public void clearUserDatabase(){
        if(!userDatabase.isEmpty()){
            userDatabase.clear();
        }
    }

    public Collection<UserData> getUserDatabase() throws DataAccessException {
        return userDatabase;
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




