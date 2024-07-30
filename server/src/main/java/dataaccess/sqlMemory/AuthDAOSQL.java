package dataaccess.sqlMemory;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthDAOSQL implements AuthDAOInterface {
    @Override
    public AuthData createAuth(UserData user) throws DataAccessException, ResponseException {
        var statement = "insert into auth (username, uuid) values (?, ?)";
        var authToken = UUID.randomUUID().toString();
        UpdateManager.executeUpdate(statement, user.getUsername(), authToken);
        return new AuthData(user.getUsername(), authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException, ResponseException {
        var statement = "select username from auth where uuid = ?";
        List<List<Object>> authQuery = UpdateManager.executeQuery(statement, authToken);
        if (!authQuery.isEmpty()) {
            List<Object> authList = authQuery.getFirst();
            AuthData authData = new AuthData(authList.get(0).toString(), authToken);
            return authData;
        }
        throw new DataAccessException("Auth token not found");
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException, ResponseException {


        var updateStatement = "delete from auth where uuid = ?";
        UpdateManager.executeUpdate(updateStatement, authToken);
    }

    @Override
    public void clearAuthDatabase() throws ResponseException, DataAccessException {
        var statement = "delete from auth";
        UpdateManager.executeUpdate(statement);
    }


    public Collection<AuthData> getAuthDatabase() throws DataAccessException, ResponseException {
        Collection<AuthData> authDataList = new ArrayList<>();
        var statement = "select * from auth";
        List<List<Object>> authQuery = UpdateManager.executeQuery(statement);
        if (!authQuery.isEmpty()) {
            throw new DataAccessException("Error: Database is not empty");
        }
        return authDataList;
    }
}
