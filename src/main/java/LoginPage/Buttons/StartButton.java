package LoginPage.Buttons;

import GamePage.GameScene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StartButton {
    public static Button startButton(Stage stage) {
        Button startButton = new Button("Start");
        startButton.setPrefSize(400, 50);
        startButton.setOnAction(event -> stage.setScene(GameScene.getGameScene()));
        return startButton;
    }
}
