package dataaccess.rammemory;


import model.UserData;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;

import java.util.ArrayList;
import java.util.Collection;


public class UserDAORAM implements UserDaoInterface {

   public Collection<UserData> userDatabase;

    public UserDAORAM() throws DataAccessException {
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

}




