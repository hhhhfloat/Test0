package view.scene.Boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class InitialBox {
    public static VBox initialBtnBox(){
        Button loginBtn = ButtonUtils.createButton("Login");
        loginBtn.setOnAction(event -> LoginCtrl.handleLogin());
        Button touristBtn = ButtonUtils.createButton("Tourist Mode");
        touristBtn.setOnAction(event -> LoginCtrl.handleTouristMode());
        Button exitBtn = ButtonUtils.createButton("Exit");
        exitBtn.setOnAction(event -> LoginCtrl.handleExit());
        VBox vBox = new VBox(10, loginBtn, touristBtn, exitBtn);
        return vBox;
    }
}
