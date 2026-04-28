package model.entity;

import model.state.GameSnapshot;

public class Account {
    String userName, password;
    //GameSnapshot saveLoad;

    public Account(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
