package view.scenes;

import controller.GameCtrl;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;
import view.game_nodes.Board;
import view.game_nodes.Interfaces.BoardInterface;
import view.game_nodes.Interfaces.ProgressLabelInterface;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GameScene extends Scene {
    public static StackPane overlayPane;

    public GameScene(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl){
        super(new BorderPane(createRoot(board, timeLabel, scoreLabel, progressLabel, gameCtrl)), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "gameSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);

    }

    private static StackPane createRoot(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {
        int bombCount = 3;
        int freezeCount = 3;
        int hintCount = 3;

        StackPane root = new StackPane();
        Pane underPane = new Pane();

        Board gameBoard = (Board) board;
        gameBoard.setLayoutX(134);
        gameBoard.setLayoutY(155);
        underPane.getChildren().add(gameBoard);

        Label timeLabelNode = (Label) timeLabel;
        Label scoreLabelNode = (Label) scoreLabel;
        Label progressLabelNode = (Label) progressLabel;

        Button pauseButton = new Button(), bombButton = new Button("Bomb: x" + bombCount), freezeButton = new Button("Freeze :x" + freezeCount), hintButton = new Button("Hint: x" + hintCount);
        pauseButton.getStyleClass().add("pausebutton");
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(15);

        pauseButton.setOnMouseClicked(event -> gameCtrl.handlePause());
        bombButton.setOnMouseClicked(event -> gameCtrl.handleBombMode());
        freezeButton.setOnMouseClicked(event -> {
            try {
                gameCtrl.handleFreeze();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        hintButton.setOnMouseClicked(event -> gameCtrl.handleHint());

        HBox stats = new HBox(10, timeLabelNode, scoreLabelNode, progressLabelNode);
        VBox utils = new VBox(100, bombButton, freezeButton, hintButton);
        stats.setLayoutX(180);
        stats.setLayoutY(20);
        utils.setLayoutX(702);
        utils.setLayoutY(230);
        underPane.getChildren().addAll(pauseButton, stats, utils);

        overlayPane = new StackPane();
        overlayPane.setMouseTransparent(true);

        root.getChildren().addAll(underPane, overlayPane);
        return root;
    }

    public static void playInformation(String info) {
        Label information = new Label(info);

        information.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-background-color: rgba(0,0,0,0.6); " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-radius: 10px;"
        );
        information.setAlignment(Pos.CENTER);
        overlayPane.getChildren().add(information);
        StackPane.setAlignment(information, Pos.CENTER);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(80), information);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(80), information);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(80), information);
        translateTransition.setFromY(-200);
        translateTransition.setToY(-280);

        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(150), information);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setDelay(Duration.seconds(0.5));

        ParallelTransition parallelTransition = new ParallelTransition(fadeInTransition, scaleTransition, translateTransition);
        SequentialTransition sequentialTransition = new SequentialTransition(information, parallelTransition, fadeOutTransition);

        sequentialTransition.setOnFinished(e -> overlayPane.getChildren().remove(information));

        sequentialTransition.play();
    }
}
