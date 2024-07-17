package Models;

public class UserData {
    private String username;
    private String password;
    private String email;

   public UserData(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    String getPassword() {
        return this.password;
    }

    String getEmail() {
        return this.email;
    }
}
