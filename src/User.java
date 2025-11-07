import java.io.Serializable;

/*
 Abstract user base. Encapsulates displayName, username, password.
*/
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String displayName;
    private String username;
    private String password;

    public User(String displayName, String username, String password) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
    }

    public String getDisplayName() { return displayName; }
    public String getUsername() { return username; }

    // login checks both username and password (caller provides username for convenience)
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // concrete roles implement menu
    public abstract void showMenu(java.util.Scanner scanner, DataStore ds);
}
