package view.scene.Boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class InitialBtnBox {
    public static VBox initialBtnBox(){
        Button loginBtn = ButtonUtils.createButton("start");
        loginBtn.setOnAction(event -> LoginCtrl.handleStart());
        Button touristBtn = ButtonUtils.createButton("start");
        touristBtn.setOnAction(event -> LoginCtrl.handlePause());
        Button exitBtn = ButtonUtils.createButton("start");
        exitBtn.setOnAction(event -> LoginCtrl.handlePause());

        VBox vBox = new VBox(10, loginBtn, touristBtn, exitBtn);
        return vBox;
    }
}
