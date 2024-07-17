package dataaccess.RamMemory;

import Models.AuthData;
import Models.UserData;
import dataaccess.AuthDAO_interface;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AuthDAO_RAM implements AuthDAO_interface {

    Collection<AuthData> authDatabase;
    UserData userData;

    public AuthDAO_RAM(Collection<AuthData> authDatabase, UserData userData) {}

    @Override
    public AuthData createAuth(UserData user) {
        AuthData authData = new AuthData(user.getUsername(), UUID.randomUUID().toString());
        authDatabase.add(authData);
        return authData;
    }

    @Override
    public AuthData getAuth(AuthData authToken) {
        return null;
    }

    @Override
    public void deleteAuth(UserData username) {

    }
}
