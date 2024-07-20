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
        if (authDatabase.isEmpty()) {
            AuthData aData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
            authDatabase.add(aData);
            return aData;
        }
        for (AuthData authData : authDatabase) {
            if (authData.getUsername().equals(user.getUsername())) {
                throw new DataAccessException("AuthToken already exists");
            }
            else{
                AuthData aData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
                authDatabase.add(aData);
                return aData;
            }
        }
        throw new DataAccessException("AuthToken already exists");
    }

    @Override
    public AuthData getAuthData(AuthData authData) {
        for (AuthData aData : authDatabase) {
            if (aData.equals(authData)){
                return aData;
            }
        }
        return null;
    }

    @Override
    public Object deleteAuth(AuthData authData) throws DataAccessException {
        for (AuthData aData : authDatabase) {
            if(aData.equals(authData)){
                authDatabase.remove(aData);
                return null;
            }
        }
        throw new DataAccessException("AuthToken does not exist");
    }

    @Override
    public void clearAuthDatabase() throws DataAccessException {
        if (!authDatabase.isEmpty()) {
            authDatabase.clear();
        }
        else{
            throw new DataAccessException("AuthDatabase is empty");
        }
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
