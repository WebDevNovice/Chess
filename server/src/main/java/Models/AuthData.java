package Models;

public class AuthData {
    private String username;
    private String authToken;

    public AuthData(String username, String authToken) {
        setUsername(username);
        setAuthToken(authToken);
    }

    public void setUsername(String username) throws IllegalArgumentException{
        if (username == null || username.equals("")) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    public void setAuthToken(String authToken) throws IllegalArgumentException{
        if (authToken == null || authToken.equals("")) {
            throw new IllegalArgumentException("AuthToken cannot be null or empty");
        }
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
