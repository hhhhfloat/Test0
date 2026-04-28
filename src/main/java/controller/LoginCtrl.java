package controller;

import dao.UserDao;
import dao.impl.FileUserDao;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Account;
import view.scenes.AccountScene;
import view.scenes.InitialScene;
import view.scenes.LoginScene;

public class LoginCtrl {
    private UserDao userDao = new FileUserDao();
    private final SceneCtrl sceneCtrl;

    public LoginCtrl(UserDao userDao, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
    }

    public void handleLogin() {
        LoginScene loginScene = new LoginScene(this);//this传递？？？解决了循环问题
        sceneCtrl.setScene(loginScene);
    }

    public void handleLoginCancel() {
        InitialScene initialScene = new InitialScene(this);
        sceneCtrl.setScene(initialScene);
    }

    public void handleLoginConfirm(String username, String password) {
        if (username.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username can't be null!");
            alert.showAndWait();
        } /*else if () {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username doesn't exist!");
            alert.showAndWait();
        }*/ else {
            if (userDao.validate(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login succeeded!");
                alert.showAndWait();
                Account account = userDao.findByUsername(username);
                showAccountScene(account, false);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Password is incorrect!");
                alert.showAndWait();
            }
        }
    }

    public void handleRegister(String username, String password) {
        if (userDao.createUser(username, password)) {
            Account account = userDao.findByUsername(username);
            showAccountScene(account, false);
        } else {
            // 通知 View 用户名已存在
        }
    }

    public void handleRegisterCancel() {
        //
    }

    public void handleRegisterConfirm() {
        //
    }

    public void handleTouristMode() {
       //
    }

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

    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait();
        //
    }

    public void handleStart() {
        //
    }

    public void handleLeaderboard() {
        VBox list = new VBox(10);
        for (int i = 1; i <= 30; i++) {
            Label menuItem = new Label("No." + i);
            menuItem.setMaxWidth(Double.MAX_VALUE);
            menuItem.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            list.getChildren().add(menuItem);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list);
        scrollPane.setFitToWidth(true);
        //scrollPane.setPrefSize(300, 300);

        Scene scene = new Scene(scrollPane, 350, 500);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setScene(scene);
        leaderboardStage.show();
    }

    public void showAccountScene(Account account, boolean isTourist) {
        AccountScene accountScene = new AccountScene(account, isTourist, this);
        sceneCtrl.setScene(accountScene);
    }

    public void showLoginScene() {
        LoginScene loginScene = new LoginScene(this);
        sceneCtrl.setScene(loginScene);
    }

    public void handleLoad1() {

    }

    public void handleLoad2() {

    }

    public void handleLoad3() {

    }
}
