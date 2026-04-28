package view.scene.Boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class AccountBox {
    public static VBox accountBtnBox(){
        Button logoutBtn = ButtonUtils.createButton("Logout");
        logoutBtn.setOnAction(event -> LoginCtrl.handleLogout());
        Button startBtn = ButtonUtils.createButton("Start");
        startBtn.setOnAction(event -> LoginCtrl.handleStart());
        Button exitBtn = ButtonUtils.createButton("Exit");
        exitBtn.setOnAction(event -> LoginCtrl.handleExit());
        VBox vBox = new VBox(10, logoutBtn, startBtn, exitBtn);
        return vBox;
    }
}
