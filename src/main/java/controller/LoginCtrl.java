package controller;

import dao.UserDao;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Account;
import model.state.ScoreEntry;
import view.scenes.AccountScene;
import view.scenes.InitialScene;
import view.scenes.LoginScene;
import view.scenes.RegisterScene;
import java.util.List;

public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final GameCtrl gameCtrl;
    private final SceneCtrl sceneCtrl;
    private Account account;

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, SceneCtrl sceneCtrl, GameCtrl gameCtrl) {
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.sceneCtrl = sceneCtrl;
        this.gameCtrl = gameCtrl;
        gameCtrl.setLoginCtrl(this);
    }

    public Account getAccount() {
        return account;
    }

    public void handleLogin() {
        showLoginScene();
        audioCtrl.playButtonSound();
    }

    public void handleLoginCancel() {
        showInitialScene();
        audioCtrl.playButtonSound();
    }

    public void handleLoginConfirm(String username, String password) {
        audioCtrl.playButtonSound();
        if (username.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username can't be null!");
            alert.showAndWait();
        } else if (!userDao.exist(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username doesn't exist!");
            alert.showAndWait();
        } else {
            if (userDao.validate(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Login succeeded!");
                alert.showAndWait();
                account = userDao.findByUsername(username);
                showAccountScene(account);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Password is incorrect!");
                alert.showAndWait();
            }
        }
    }

    public void handleTouristMode() {
        audioCtrl.playButtonSound();
        showAccountScene();
    }

    public void handleExit() {
        audioCtrl.playButtonSound();
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

    public void handleRegister() {
        audioCtrl.playButtonSound();
        showRegisterScene();
    }

    public void handleRegisterCancel() {
        audioCtrl.playButtonSound();
        sceneCtrl.setScene(new LoginScene(this));
    }

    public void handleRegisterConfirm(String username, String password) {
        audioCtrl.playButtonSound();
        if(userDao.exist(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Username's already used!");
            alert.showAndWait();
        }
        else if(username.length() > 1000){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please use a shorter name!\n***Shorten your name with \n&❂*…←…鳼№茡洟丗▦©∭");
            alert.showAndWait();
        }
        else {
            userDao.createUser(username, password);
            Account account = userDao.findByUsername(username);
            showAccountScene(account);
        }
    }

    public void handleStart() {
        audioCtrl.playButtonSound();
        if (account != null) {
            gameCtrl.showLoadScene();
        } else {
            gameCtrl.setLoadNumber(0);
            gameCtrl.handleLoad0();
        }
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                account = null;
                gameCtrl.setLoadNumber(0);
                showInitialScene();
            }
        });
    }

    public void handleLeaderboard() {
        audioCtrl.playButtonSound();
        VBox list = new VBox(10);
        List<ScoreEntry> userList = userDao.getLeaderboard(30);
        for (int i = 1; i <= userList.toArray().length; i++) {
            Label menuItem = new Label(String.format("No.%d: %s   Score:%d", i, userList.get(i-1).getName(), userList.get(i-1).getScore()));
            menuItem.setMaxWidth(Double.MAX_VALUE);
            menuItem.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            list.getChildren().add(menuItem);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(300, 300);

        Scene scene = new Scene(scrollPane, 350, 500);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setScene(scene);
        leaderboardStage.show();
    }

    public void showInitialScene() {
        sceneCtrl.setScene(new InitialScene(this));
    }
    public void showLoginScene() {
        sceneCtrl.setScene(new LoginScene(this));
    }
    public void showRegisterScene() {
        sceneCtrl.setScene(new RegisterScene(this));
    }
    public void showAccountScene() {
        sceneCtrl.setScene(new AccountScene(this));
    }
    public void showAccountScene(Account account) {
        gameCtrl.setAccount(account);
        sceneCtrl.setScene(new AccountScene(this));
    }
}
