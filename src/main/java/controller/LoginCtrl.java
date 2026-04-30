package controller;

import dao.UserDao;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.entity.Account;
import view.scenes.AccountScene;
import view.scenes.InitialScene;
import view.scenes.LoginScene;
import view.scenes.RegisterScene;

public class LoginCtrl {
    private final UserDao userDao;
    private GameCtrl gameCtrl;
    private final SceneCtrl sceneCtrl;

    public LoginCtrl(UserDao userDao, SceneCtrl sceneCtrl, GameCtrl gameCtrl) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
        this.gameCtrl = gameCtrl;
    }

    public void handleLogin() { showLoginScene(); }

    public void handleLoginCancel() { showInitialScene(); }

    public void handleLoginConfirm(String username, String password) {
        if (username.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username can't be null!");
            alert.showAndWait();
        } else if (userDao.exist(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username doesn't exist!");
            alert.showAndWait();
        } else {
            if (userDao.validate(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login succeeded!");
                alert.showAndWait();
                Account account = userDao.findByUsername(username);
                showAccountScene(account);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Password is incorrect!");
                alert.showAndWait();
            }
        }
    }

    public void handleTouristMode() { showAccountScene(); }

    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("All the unsaved data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    public void handleRegister() { showRegisterScene(); }

    public void handleRegisterCancel() { sceneCtrl.setScene(new LoginScene(this)); }

    public void handleRegisterConfirm(String username, String password) {
        if(userDao.exist(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Username's already used!");
            alert.showAndWait();
        } else {
            userDao.createUser(username, password);
            Account account = userDao.findByUsername(username);
            showAccountScene(account);
        }
    }

    public void showInitialScene() { sceneCtrl.setScene(new InitialScene(this)); }

    public void showLoginScene() { sceneCtrl.setScene(new LoginScene(this)); }

    public void showRegisterScene() { sceneCtrl.setScene(new RegisterScene(this)); }

    public void showAccountScene() { sceneCtrl.setScene(new AccountScene(gameCtrl)); }

    public void showAccountScene(Account account) {
        gameCtrl.setAccount(account);
        sceneCtrl.setScene(new AccountScene(account, gameCtrl));
    }
}
