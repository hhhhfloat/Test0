package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.entity.Account;

public class AccountBox extends VBox {
    private final Button startBtn = new Button("Start");
    private final Button logoutBtn = new Button("Logout");
    private final Button leaderboardBtn = new Button("Leaderboard");
    private final Button exitBtn = new Button("Exit");
    private final Label welcomeLabel = new Label();

    public AccountBox(GameCtrl gameCtrl) {
        super(15);
        welcomeLabel.setText("Tourist Mode");
        setAction(gameCtrl);
        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, exitBtn);
    }

    public AccountBox(Account account, GameCtrl gameCtrl) {
        super(15);
        welcomeLabel.setText("Welcome, " + account.getUserName());
        setAction(gameCtrl);
        getChildren().addAll(welcomeLabel, startBtn, logoutBtn, leaderboardBtn, exitBtn);
    }

    public void setAction(GameCtrl gameCtrl) {
        startBtn.setOnAction(event -> gameCtrl.handleStart());
        logoutBtn.setOnAction(event -> gameCtrl.handleLogout());
        exitBtn.setOnAction(event -> gameCtrl.handleExit());
        leaderboardBtn.setOnAction(event -> gameCtrl.handleLeaderboard());
    }
}
