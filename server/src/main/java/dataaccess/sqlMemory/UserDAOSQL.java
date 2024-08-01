package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import dataaccess.UserDaoInterface;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDAOSQL implements UserDaoInterface {


    public UserDAOSQL() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createUserTable();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

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
            throw new DataAccessException("Error: User not found");
        }

        List<Object> userData = queryList.getFirst();
        if (BCrypt.checkpw(user.getPassword(), userData.getFirst().toString())) {
            return new UserData(user.getUsername(), null,null);
        }else {
            throw new ResponseException(401,"Error: Wrong password");
        }
    }

    @Override
    public void clearUserDatabase() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM user";
        UpdateManager.executeUpdate(statement);
    }

    @Override
    public Collection<UserData> getUserDatabase() throws DataAccessException, ResponseException {
        Collection<UserData> userDataCollection = new ArrayList<>();
        var statement = "SELECT * FROM user";
        List<List<Object>> queryList = UpdateManager.executeQuery(statement);
        for (List<Object> query : queryList) {
            UserData userData = new UserData(query.get(0).toString(),null,null);
            userDataCollection.add(userData);
        }
        return userDataCollection;
    }

}
