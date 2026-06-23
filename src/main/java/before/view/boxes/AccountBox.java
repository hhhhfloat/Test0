package before.view.boxes;

import before.controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modifying.auth.model.Account;

public class AccountBox extends VBox {
    public AccountBox(LoginCtrl loginCtrl) {
        super(15);

        Account acc = loginCtrl.getAccount();
        boolean isTourist = loginCtrl.isTourist();

        Label welcomeLabel = new Label();
        welcomeLabel.setText(isTourist?"Welcome":("Welcome, " + acc.getUserName()));

        Button startBtn = new Button("Start"),
                logoutBtn = new Button(isTourist?"Leave":"Logout"),
                leaderboardBtn = new Button("Leaderboard"),
                exitBtn = new Button("Exit");

        startBtn.setOnAction(event -> loginCtrl.handleStart());
        logoutBtn.setOnAction(event -> loginCtrl.handleLogout());
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        leaderboardBtn.setOnAction(event -> loginCtrl.handleLeaderboard());


        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, exitBtn);

    }
}
