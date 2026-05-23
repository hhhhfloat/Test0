package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class InitialBox extends VBox{
    public InitialBox(LoginCtrl loginCtrl) {
        super(15);
        Button loginBtn = new Button("Login"),
                touristBtn = new Button("Tourist Mode"),
                exitBtn = new Button("Exit");
        loginBtn.setOnAction(event -> loginCtrl.handleLogin());
        touristBtn.setOnAction(event -> loginCtrl.handleTouristMode());
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        getChildren().addAll(loginBtn, touristBtn, exitBtn);
    }
}
