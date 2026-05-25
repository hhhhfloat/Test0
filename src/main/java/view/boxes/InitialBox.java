package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InitialBox extends VBox{
    public InitialBox(LoginCtrl loginCtrl) {
        super(170);
        getStyleClass().add("initialbox");
        Label link = new Label("Link Link!");
        Button loginBtn = new Button("Login"),
                touristBtn = new Button("Tourist Mode"),
                exitBtn = new Button("Exit");
        loginBtn.setOnAction(event -> loginCtrl.handleLogin());
        touristBtn.setOnAction(event -> loginCtrl.handleTouristMode());
        exitBtn.setOnAction(event -> loginCtrl.handleExit());
        VBox buttonBox = new VBox(60, loginBtn, touristBtn, exitBtn);
        buttonBox.getStyleClass().add("initialbox");
        getChildren().addAll(link, buttonBox);
    }
}
