package dataaccess.RamMemory;


import Models.AuthData;
import Models.UserData;
import dataaccess.UserDao_interface;
import jdk.jshell.spi.ExecutionControl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;

public class UserDAO_RAM implements UserDao_interface {
    HashMap <Integer, UserData> userDatabase;
    UserData userData;
    Integer userId = 0;
    //delete below later
    public String my_name = "Jake";
    public String my_email = "jacobgbullock3@gmail.com";
    public String my_password = "12345";
    public UserData test = new UserData(my_name, my_password,my_email);
    //delete above

    public UserDAO_RAM(UserData userData) {
        this.userData = userData;
        this.userDatabase = new HashMap<>();
    }

    public void initializeUserDatabase() {
        userDatabase.put(userId++, test);
    }

    @Override
    public AuthData createUser(UserData userData) {
        return null;
    }

    @Override
    public AuthData getUser(UserData userData) {
        return null;
    }

    @Override
    public void deleteUser(UserData userData) {

    }


    //use main to create the reference of the hashMap

}




