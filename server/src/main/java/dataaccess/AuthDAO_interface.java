package dataaccess;

import Models.AuthData;
import Models.UserData;
import dataaccess.RamMemory.AuthDAO_RAM;

import java.util.Collection;

public interface AuthDAO_interface {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuth(AuthData authToken) throws DataAccessException;
    void deleteAuth(UserData username) throws DataAccessException;
}
