package dataaccess.sqlMemory;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import model.UserData;

import java.util.Collection;
import java.util.List;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserDAOSQL implements UserDaoInterface {
    @Override
    public UserData createUser(UserData userData) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
        var json = new Gson().toJson(userData);
        var id = UpdateManager.executeUpdate(statement, userData.getUsername(), userData.getPassword(), userData.getEmail());
        return userData;
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException, ResponseException {
        var statement = "SELECT username FROM users WHERE username = ?";
        var usernameID = UpdateManager.executeUpdate(statement, user.getUsername());
        return user;
    }

    @Override
    public void clearUserDatabase() throws ResponseException, DataAccessException {
        var statment = "DELETE FROM users";
        UpdateManager.executeUpdate(statment);
    }

    @Override
    public Collection<UserData> getUserDatabase() throws DataAccessException {
        return List.of();
    }

}
