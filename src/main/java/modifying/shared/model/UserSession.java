package modifying.shared.model;

public class UserSession {
    private static UserSession instance;
    private String currentUsername;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    public void setUser(String username) {
        this.currentUsername = username;
    }

    public String getUsername() {
        return currentUsername;
    }

    public void clear() {
        currentUsername = null;
    }
}