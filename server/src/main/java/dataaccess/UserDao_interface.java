package dataaccess;

import Models.AuthData;
import Models.UserData;

public interface UserDao_interface{
    AuthData createUser(UserData userData);
    AuthData getUser(UserData userData);
    void deleteUser(UserData userData);
}
