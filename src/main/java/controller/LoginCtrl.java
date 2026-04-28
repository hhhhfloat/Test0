package controller;

import LoginPage.Account;

public class LoginCtrl {
    SceneCtrl sceneCtrl = new SceneCtrl();

    public void setSceneCtrl(SceneCtrl sceneCtrl) {
        this.sceneCtrl = sceneCtrl;
    }

    public void handleLogin(String usename, String password) {
        sceneCtrl.switchToLogin();
    }

    public void handleRegister(String username, String password) {
        sceneCtrl.switchToRegister();
    }

    public void handleTouristMode() {
        sceneCtrl.switchToAccount();
    }

    public void handleLogout() {
        sceneCtrl.switchToLogin();
    }

    public void handleStartGame() {
        sceneCtrl.switchToLevelSelect();
    }

    public void handleLeaderboard() {

    }

    public boolean isLoggedIn() {
        return true;
    }

    public Account getCurrentAccount() {
        return new Account();
    }
}
