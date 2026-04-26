package GamePage.Buttons;

import GamePage.GameScene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Eazy {
    public static Button eazy(Stage stage) {
        Button eazyMode = new Button("EAZY");
        eazyMode.setPrefSize(600, 100);
        eazyMode.setOnAction(event -> stage.setScene(GameScene.getGameScene(0)));
        return eazyMode;
    }
}
