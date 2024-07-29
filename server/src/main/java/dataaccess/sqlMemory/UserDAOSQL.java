package dataaccess.sqlMemory;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import model.UserData;

import java.util.Collection;
import java.util.List;

public class UserDAOSQL implements UserDaoInterface {
    @Override
    public UserData createUser(UserData userData) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
        var json = new Gson().toJson(userData);
        var id = UpdateManager.executeUpdateForInteger(statement, userData.getUsername(), userData.getPassword(), userData.getEmail());
        return userData;
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException, ResponseException {
        var statement = "SELECT username FROM users WHERE username = ?";
        var usernameID = UpdateManager.executeUpdateForInteger(statement, user.getUsername());
        return user;
    }

    @Override
    public void clearUserDatabase() throws ResponseException, DataAccessException {
        var statment = "DELETE FROM users";
        UpdateManager.executeUpdateForInteger(statment);
    }

    @Override
    public Collection<UserData> getUserDatabase() throws DataAccessException {
        return List.of();
    }

}
