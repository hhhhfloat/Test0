package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class InitialBox extends VBox{
    public InitialBox(LoginCtrl loginCtrl) {
        super(15);
        Button loginBtn = ButtonUtils.createButton("Login");
        loginBtn.setOnAction(event -> loginCtrl.handleLogin());
        Button touristBtn = ButtonUtils.createButton("Tourist Mode");
        touristBtn.setOnAction(event -> loginCtrl.handleTouristMode());
        Button exitBtn = ButtonUtils.createButton("Exit");
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        getChildren().addAll(loginBtn, touristBtn, exitBtn);
    }
}
