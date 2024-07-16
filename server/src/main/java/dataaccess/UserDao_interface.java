package dataaccess;

import dataaccess.RamMemory.UserDAO_RAM;

public interface UserDao_interface <T> {
    T createUser(T user);
    T getUser(T user);
    Boolean findUser(T user);
    void deleteUser(T user);
}
