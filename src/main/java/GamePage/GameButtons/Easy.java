package GamePage.GameButtons;

import GamePage.GameScene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Easy {
    public static Button easy(Stage stage) {
        Button easyMode = new Button("EASY");
        easyMode.setPrefSize(600, 100);
        easyMode.setOnAction(event -> stage.setScene(GameScene.getGameScene()));
        return easyMode;
    }
}
