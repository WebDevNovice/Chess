package dataaccess.RamMemory;


import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;
import dataaccess.UserDao_interface;
import jdk.jshell.spi.ExecutionControl;

import java.io.FileNotFoundException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UserDAO_RAM implements UserDao_interface {

   Collection<UserData> userDatabase;
    UserData userData;
    Integer userId = 0;
    //delete below later

    //delete above

    public UserDAO_RAM() throws DataAccessException {
        this.userData = userData;
        this.userDatabase = new ArrayList<>();
        String my_name = "Jake";
        String my_email = "jacobgbullock3@gmail.com";
        String my_password = "12345";
        UserData test = new UserData(my_name, my_password,my_email);
        userDatabase.add(test);
    }



    @Override
    public AuthData createUser(UserData userData) {
        return null;
    }

    @Override
    public boolean getUser(UserData userData) {
        return userDatabase.containsValue(userData);
    }

    @Override
    public void deleteUser(UserData userData) {

    }


    //use main to create the reference of the hashMap

}




