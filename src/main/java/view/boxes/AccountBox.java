package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.entity.Account;

public class AccountBox extends VBox {
    public AccountBox(Account account, boolean isTourist, LoginCtrl loginCtrl) {
        super(15);

        Label welcomeLabel = new Label();
        if (isTourist) {
            welcomeLabel.setText("Tourist Mode");
        } else {
            welcomeLabel.setText("Welcome, " + account.getUserName());
        }
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button startBtn = new Button("Start");
        Button logoutBtn = new Button("Logout");

        startBtn.setOnAction(e -> {
            // sceneCtrl.setScene(new GameScene(...));
        });

        logoutBtn.setOnAction(e -> loginCtrl.showLoginScene());

        getChildren().addAll(welcomeLabel, startBtn, logoutBtn);
    }
}
