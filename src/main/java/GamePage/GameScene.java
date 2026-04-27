package GamePage;

import GamePage.Buttons.Pause;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class GameScene {
    public static Scene getGameScene() {
        int[][] board = {{1,2,3,4},{5,6,7,8},{1,2,3,4},{5,6,7,8}};
        VBox gameboard = new VBox(Grid.grid(10, 10, 50, 50));
        int points = 0;
        ScoreLabel scoreLabel = new ScoreLabel(points);
        TimerLabel timerLabel = new TimerLabel();
        gameboard.getChildren().addAll(Pause.getButton(), scoreLabel.getPointsLabel(), timerLabel.timer());
        return new Scene(gameboard);
    }
}
