package view.scene;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import view.common.ButtonUtils;

public class GameScene {
    public static Scene gameScene()
    {

        Button pauseBtn = ButtonUtils.createButton("pause");
        pauseBtn.setOnAction(e -> GameCtrl.handlePause());

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
