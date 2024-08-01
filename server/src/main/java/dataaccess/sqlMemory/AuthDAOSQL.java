package dataaccess.sqlMemory;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthDAOSQL implements AuthDAOInterface {

    public AuthDAOSQL() {
        try{
            DatabaseManager.createDatabase();
            DatabaseManager.createAuthTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO auth (username, uuid) VALUES (?, ?)";
        var authToken = UUID.randomUUID().toString();
        UpdateManager.executeUpdate(statement, user.getUsername(), authToken);
        return new AuthData(user.getUsername(), authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException, ResponseException {
        AuthData authData = null;
        var statement = "select * from auth where uuid = ?";
        List<List<Object>> authQuery = UpdateManager.executeQuery(statement, authToken);
        if (!authQuery.isEmpty()) {
            for (List<Object> row : authQuery) {
                authData = new AuthData(row.get(0).toString(), row.get(1).toString());
            }
            return authData;
        }
        throw new DataAccessException("Error: Auth token not found");
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException, ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new DataAccessException("Error: Auth token not found");
        }
        else{
            var updateStatement = "DELETE from auth where uuid = ?";
            UpdateManager.executeUpdate(updateStatement, authToken);
        }
    }

    @Override
    public void clearAuthDatabase() throws ResponseException, DataAccessException {
        var clearStatement = "delete from auth";
        UpdateManager.executeUpdate(clearStatement);

        var verifyStatement = "select * from auth";
        List<List<Object>> authQuery = UpdateManager.executeQuery(verifyStatement);

        if (!authQuery.isEmpty()) {
            throw new DataAccessException("Database not empty");
        }
    }


    public Collection<AuthData> getAuthDatabase() throws DataAccessException, ResponseException {
        Collection<AuthData> authDataList = new ArrayList<>();
        var statement = "select * from auth";
        List<List<Object>> authQuery = UpdateManager.executeQuery(statement);
        if (!authQuery.isEmpty()) {
            throw new DataAccessException("Error: Database is not empty");
        }
        for (List<Object> authList : authQuery) {
            AuthData authData = new AuthData(authList.get(0).toString(), authList.get(1).toString());
            authDataList.add(authData);
        }
        return authDataList;
    }
}
