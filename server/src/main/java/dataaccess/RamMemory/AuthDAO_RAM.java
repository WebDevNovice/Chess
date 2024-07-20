package dataaccess.RamMemory;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class AuthDAO_RAM implements AuthDAO_interface {

    Collection<AuthData> authDatabase;

    public AuthDAO_RAM() {
        authDatabase = new ArrayList<>();
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        if (user.getUsername() == null || user.getPassword() == null ||
                user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new DataAccessException("Username and password are required");
        }
        else {
            AuthData aData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
            authDatabase.add(aData);
            return aData;
        }
    }

    @Override
    //take in a string
    public AuthData getAuthData(String authToken) throws DataAccessException {
        for (AuthData aData : authDatabase) {
            if (aData.getAuthToken().equals(authToken)){
                return aData;
            }
        }
        throw new DataAccessException("AuthToken does not exist");
    }

    @Override
    public Object deleteAuth(String authToken) throws DataAccessException {
        if (getAuthData(authToken) != null) {
            authDatabase.remove(getAuthData(authToken));
            return "";
        }
        else {
            throw new DataAccessException("AuthToken does not exist");
        }
    }

    @Override
    public void clearAuthDatabase() {
            authDatabase.clear();
    }

    public Collection<AuthData> getAuthDatabase() throws DataAccessException{
        return authDatabase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthDAO_RAM that = (AuthDAO_RAM) o;
        return Objects.equals(authDatabase, that.authDatabase);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authDatabase);
    }
}
