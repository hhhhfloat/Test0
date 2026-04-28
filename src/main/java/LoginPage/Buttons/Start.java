package LoginPage.Buttons;

import GamePage.Scenes.LevelScene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Start {
    public static Button startButton(Stage stage) {
        Button startButton = new Button("Start");
        startButton.setPrefSize(400, 50);
        startButton.setOnAction(event -> stage.setScene(LevelScene.getLevelScene(stage)));
        return startButton;
    }
}
