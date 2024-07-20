package Models;

import java.util.Objects;

public class AuthData {
    private String username;
    private String authToken;

    public AuthData(String username, String authToken) {
        setUsername(username);
        setAuthToken(authToken);
    }

    public void setUsername(String username) throws IllegalArgumentException{
        this.username = username;
    }

    public void setAuthToken(String authToken) throws IllegalArgumentException{
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "username='" + username + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }
}
