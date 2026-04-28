package view.scene;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class LoginScene {
    public static Scene loginScene()
    {
        Button loginBtn = ButtonUtils.createButton("start");
        loginBtn.setOnAction(e -> GameCtrl.handlePause());
        Button touristBtn = ButtonUtils.createButton("start");
        touristBtn.setOnAction(e -> GameCtrl.handlePause());
        Button exitBtn = ButtonUtils.createButton("start");
        exitBtn.setOnAction(e -> GameCtrl.handlePause());
        VBox list = new VBox(10, loginBtn, touristBtn, exitBtn);
        Scene scene = new Scene(list);
        return scene;
    }
}
