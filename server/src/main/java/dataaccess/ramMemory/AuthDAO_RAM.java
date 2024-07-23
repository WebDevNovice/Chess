package dataaccess.ramMemory;

import model.AuthData;
import model.UserData;
import service.BadRequestException;
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
            AuthData authData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
            authDatabase.add(authData);
            return authData;
    }

    @Override
    //take in a string
    public AuthData getAuthData(String authToken) throws DataAccessException {
        for (AuthData authData : authDatabase) {
            if (authData.getAuthToken().equals(authToken)){
                return authData;
            }
        }
        throw new DataAccessException("Error: AuthToken does not exist");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, BadRequestException {
        boolean found = false;
        for (AuthData authData : authDatabase) {
            if (authData.getAuthToken().equals(authToken)){
                found = true;
            }
        }
        if (found){
            authDatabase.remove(getAuthData(authToken));
        }
        else{
            throw new DataAccessException("Error: AuthToken does not exist");
        }
    }

    @Override
    public void clearAuthDatabase() {
            if (!authDatabase.isEmpty()){
                authDatabase.clear();
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
