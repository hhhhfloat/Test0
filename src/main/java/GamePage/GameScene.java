package GamePage;

import GamePage.Buttons.Pause;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameScene {
    public static Scene getGameScene() {
        VBox gameboard = new VBox(Map.grid(10,10,50,50));
        gameboard.getChildren().add(Pause.getButton());
        Scene gameScene = new Scene(gameboard);
        return gameScene;
    }
}
