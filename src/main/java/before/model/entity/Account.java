package before.model.entity;


import before.controller.LoginCtrl;

public class Account {
    String userName, password, safeUserName;

    public Account(String userName) {
        this.userName = userName;
        this.safeUserName = LoginCtrl.properName(userName);
    }

    public String getUserName() {
        return userName;
    }

    public String getSafeUserName() {
        return safeUserName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
