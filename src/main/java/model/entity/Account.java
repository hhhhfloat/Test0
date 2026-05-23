package model.entity;



public class Account {
    String userName, password;

    public Account(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
