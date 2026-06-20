package modifying.auth.view.box;

import before.model.entity.Account;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

public class AccountBox extends VBox {

    private LoginCtrl loginCtrl;

    private Label welcomeLabel;
    private Button startBtn, logoutBtn, leaderboardBtn, quitBtn;

    public AccountBox(LoginCtrl loginCtrl) {
        super(15);

        this.loginCtrl = loginCtrl;

        welcomeLabel = new Label();
        welcomeLabel.setText("Welcome, " + loginCtrl.getAccount().getUserName());

        startBtn = new Button("Start");
        logoutBtn = new Button("Logout");
        leaderboardBtn = new Button("Leaderboard");
        quitBtn = new Button("Exit");

        // startBtn.setOnAction(event -> loginCtrl.handleStart());
        logoutBtn.setOnAction(event -> loginCtrl.handleLogout());
        quitBtn.setOnAction(event -> loginCtrl.handleQuit());
        // leaderboardBtn.setOnAction(event -> loginCtrl.handleLeaderboard());


        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, quitBtn);
    }
    public void syncAccount(){
        welcomeLabel.setText("Welcome, " + loginCtrl.getAccount().getUserName());
    }
}
