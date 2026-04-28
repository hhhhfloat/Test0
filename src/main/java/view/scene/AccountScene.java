package view.scene;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class AccountScene {
    public static Scene accountScene()
    {
        Button startBtn = ButtonUtils.createButton("start");
        startBtn.setOnAction(e -> GameCtrl.handlePause());
        Button logoutBtn = ButtonUtils.createButton("start");
        logoutBtn.setOnAction(e -> GameCtrl.handlePause());
        Button leaderboardBtn = ButtonUtils.createButton("start");
        startBtn.setOnAction(e -> GameCtrl.handlePause());
        Button exitBtn = ButtonUtils.createButton("start");
        startBtn.setOnAction(e -> GameCtrl.handlePause());
        VBox list = new VBox(10, startBtn, logoutBtn, leaderboardBtn, exitBtn);
        Scene scene = new Scene(list);
        return scene;
    }
}
