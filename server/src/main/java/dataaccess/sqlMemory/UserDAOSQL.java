package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.List;

public class UserDAOSQL implements UserDaoInterface {
    @Override
    public UserData createUser(UserData userData) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?,?,?)";
        String hashedPassword = BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt());
        UpdateManager.executeUpdate(statement, userData.getUsername(), hashedPassword, userData.getEmail());
        return userData;
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException, ResponseException {
        var statement = "SELECT password FROM user WHERE username = ?";
        List<List<Object>> queryList = UpdateManager.executeQuery(statement, user.getUsername());
        if (queryList.isEmpty()) {
            throw new DataAccessException("User not found");
        }

        List<Object> userData = queryList.getFirst();
        if (BCrypt.checkpw(user.getPassword(), userData.getFirst().toString())) {
            return new UserData(user.getUsername(), null,null);
        }else {
            throw new ResponseException(401,"Wrong password");
        }
    }

    @Override
    public void clearUserDatabase() throws ResponseException, DataAccessException {
        var statment = "DELETE FROM user";
        UpdateManager.executeUpdate(statment);
    }

    @Override
    public Collection<UserData> getUserDatabase() throws DataAccessException {
        return List.of();
    }

}
