package ButtonToBeChanged.GameButtons;

import GamePage.Points;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Difficult {
    public static Button difficult(Stage stage) {
        Button difficultMode  = new Button("DIFFICULT");
        difficultMode.setPrefSize(600, 100);
        difficultMode.setOnAction(event -> stage.setScene(Points.GameScene.getGameScene()));
        return difficultMode;
    }
}
