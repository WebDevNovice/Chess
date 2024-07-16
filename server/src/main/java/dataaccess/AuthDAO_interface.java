package dataaccess;

import dataaccess.RamMemory.AuthDAO_RAM;

public interface AuthDAO_interface <T> {
    T createAuth(T authData);
    T getAuth(T authData);
    void deleteAuth(T authData);
}
