package dataaccess.RamMemory;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;
import dataaccess.DataAccessException;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AuthDAO_RAM implements AuthDAO_interface {

    Collection<AuthData> authDatabase;

    public AuthDAO_RAM() {
        authDatabase = new ArrayList<>();
    }

    @Override
    public AuthData createAuth(UserData user) {
        AuthData authData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
        authDatabase.add(authData);
        return authData;
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
}
