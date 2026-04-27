package GamePage;

import GamePage.Buttons.Pause;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameScene {
    public static Scene getGameScene() {
        BorderPane gameboard = new BorderPane(Grid.grid(10, 10, 50, 50));
        int points = 0;
        ScoreLabel scoreLabel = new ScoreLabel(points);
        TimerLabel timerLabel = new TimerLabel();
        gameboard.setBottom(timerLabel.timer());
        gameboard.setTop(scoreLabel.getPointsLabel());
        return new Scene(gameboard,1600, 900);
    }
}
