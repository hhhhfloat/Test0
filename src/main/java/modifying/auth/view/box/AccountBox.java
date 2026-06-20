package modifying.auth.view.box;

import before.model.entity.Account;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

public class AccountBox extends VBox {
    public AccountBox(LoginCtrl loginCtrl) {
        super(15);

        Account acc = loginCtrl.getAccount();

        Label welcomeLabel = new Label();
        welcomeLabel.setText("Welcome, " + acc.getUserName());

        Button startBtn = new Button("Start"),
                logoutBtn = new Button("Logout"),
                leaderboardBtn = new Button("Leaderboard"),
                quitBtn = new Button("Exit");

        // startBtn.setOnAction(event -> loginCtrl.handleStart());
        logoutBtn.setOnAction(event -> loginCtrl.handleLogout());
        quitBtn.setOnAction(event -> loginCtrl.handleQuit());
        // leaderboardBtn.setOnAction(event -> loginCtrl.handleLeaderboard());


        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, quitBtn);

    }
}
