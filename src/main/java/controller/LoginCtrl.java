package controller;

import LoginPage.Account;
import LoginPage.AccountScene;
import javafx.scene.control.Alert;

public class LoginCtrl {
    public static void handleLogin() {
        SceneCtrl.switchToLogin();
    }

    public static void handleLoginCancel() {
        SceneCtrl.switchToInitial();
    }

    public static void handleLoginConfirm(String account, String password) {
        if (account.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username can't be null!");
            alert.showAndWait();
        } else if (!users.containsKey(account)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username doesn't exist!");
            alert.showAndWait();
        } else {
            if (password.equals(users.get(account))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login succeeded!");
                alert.showAndWait();
                account.setAccountName(account);

                SceneCtrl.switchToAccount();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Password is incorrect!");
                alert.showAndWait();
            }
        }
    }

    public static void handleRegister() {
        SceneCtrl.switchToRegister();
    }

    public static void handleRegisterCancel() {
        SceneCtrl.switchToLogin();
    }

    public static void handleRegisterConfirm() {
        SceneCtrl.switchToAccount();
    }

    public static void handleTouristMode() {
        SceneCtrl.switchToAccount();
    }

    public static void handleExit() {

    }

    public static void handleLogout() {
        SceneCtrl.switchToLogin();
    }

    public static void handleStart() {
        SceneCtrl.switchToLevelSelect();
    }

    public static void handleLeaderboard() {

    }

    public boolean isLoggedIn() {
        return true;
    }

    public Account getCurrentAccount() {
        return new Account();
    }
}
