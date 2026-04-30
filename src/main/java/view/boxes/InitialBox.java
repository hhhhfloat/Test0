package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class InitialBox extends VBox{
    public InitialBox(LoginCtrl loginCtrl) {
        super(15);
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(event -> loginCtrl.handleLogin());
        Button touristBtn = new Button("Tourist Mode");
        touristBtn.setOnAction(event -> loginCtrl.handleTouristMode());
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        getChildren().addAll(loginBtn, touristBtn, exitBtn);
    }
}
