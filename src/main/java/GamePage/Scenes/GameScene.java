package GamePage.Scenes;

import GamePage.Buttons.Pause;
import GamePage.Board;
import GamePage.Labels.ScoreLabel;
import GamePage.Labels.TimerLabel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GameScene {
    public static Scene getGameScene() {
        Board board = new Board();
        BorderPane gameboard = new BorderPane(board.grid(10, 10, 50, 50));
        int points = 0;
        ScoreLabel scoreLabel = new ScoreLabel(points);
        TimerLabel timerLabel = new TimerLabel();
        Label timer = timerLabel.timer(),score=scoreLabel.getPointsLabel();
        score.setMaxWidth(Double.MAX_VALUE);
        score.setAlignment(Pos.CENTER);
        gameboard.setTop(score);

        Button pause = Pause.getButton();
        pause.setAlignment(Pos.CENTER_RIGHT);

        timer.setMaxWidth(Double.MAX_VALUE);
        timer.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(timer,pause);
        HBox.setHgrow(timer, Priority.ALWAYS);
        gameboard.setBottom(hBox);

        return new Scene(gameboard,1200, 900);
    }
}
