package dataaccess.sqlMemory;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.execeptions.BadRequestException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthDAOSQL implements AuthDAOInterface {
    @Override
    public AuthData createAuth(UserData user) throws DataAccessException, ResponseException {
        var statement = "insert into auth (username, uuid) values (?, ?)";
        var authToken = UUID.randomUUID().toString();
        var id = UpdateManager.executeUpdateForInteger(statement, user.getUsername(), authToken);
        return new AuthData(user.getUsername(), authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException, BadRequestException {
        var statement = "select uuid from auth where uuid = ?";
        if (statement != null){
            return
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, BadRequestException {

    }

    @Override
    public void clearAuthDatabase() {

    }

    @Override
    public Collection<AuthData> getAuthDatabase() throws DataAccessException {
        return List.of();
    }
}
