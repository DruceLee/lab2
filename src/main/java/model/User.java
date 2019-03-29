package model;

public class User {
    private String login;
    private String password;
    private boolean isBanned;

    public User(String login, String password, boolean isBanned) {
        this.login = login;
        this.password = password;
        this.isBanned = isBanned;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isBanned() {
        return isBanned;
    }
}
