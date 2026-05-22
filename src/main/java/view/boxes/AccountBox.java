package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AccountBox extends VBox {
    public AccountBox(LoginCtrl loginCtrl) {
        super(15);

        Label welcomeLabel = new Label();
        welcomeLabel.setText(loginCtrl.getAccount() == null?"Welcome":("Welcome, " + loginCtrl.getAccount().getUserName()));

        Button startBtn = new Button("Start"), logoutBtn = new Button("Logout"), leaderboardBtn = new Button("Leaderboard"), exitBtn = new Button("Exit");
        startBtn.setOnAction(event -> loginCtrl.handleStart());
        logoutBtn.setOnAction(event -> loginCtrl.handleLogout());
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        leaderboardBtn.setOnAction(event -> loginCtrl.handleLeaderboard());

        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, exitBtn);
    }
}
