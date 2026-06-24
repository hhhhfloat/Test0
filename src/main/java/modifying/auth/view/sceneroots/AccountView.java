package modifying.auth.view.sceneroots;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

import java.nio.file.Paths;

public class AccountView extends StackPane {

    private final VBox accountBox;
    private final LoginCtrl loginCtrl;
    private Label welcomeLabel;
    private Button startBtn, logoutBtn, quitBtn;

    public AccountView(LoginCtrl loginCtrl) {
        this.loginCtrl = loginCtrl;

        accountBox = new VBox(15);
        accountBox.setAlignment(Pos.CENTER);

        initAccountBox();

        getChildren().add(accountBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "AuthSceneStyle", "authSceneStyle.css").toUri().toString());
    }

    private void initAccountBox() {
        welcomeLabel = new Label();
        welcomeLabel.setText("Welcome, " + loginCtrl.getAccount().getUserName());

        startBtn = new Button("Start");
        logoutBtn = new Button("Logout");
        quitBtn = new Button("Exit");

        // 🔥 为所有按钮添加样式类
        startBtn.getStyleClass().add("action-button");
        logoutBtn.getStyleClass().add("action-button");
        quitBtn.getStyleClass().add("action-button");

        startBtn.setOnAction(event -> loginCtrl.handleStart());
        logoutBtn.setOnAction(event -> loginCtrl.handleLogout());
        quitBtn.setOnAction(event -> loginCtrl.handleQuit());

        accountBox.getChildren().addAll(welcomeLabel, startBtn, logoutBtn, quitBtn);
    }

    public void syncUser() {
        welcomeLabel.setText("Welcome, " + loginCtrl.getAccount().getUserName());
    }
}