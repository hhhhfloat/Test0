package view.scenes;

import controller.GameCtrl;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    public GameScene(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {
        super(new BorderPane(createRoot(board, timeLabel, scoreLabel, progressLabel, gameCtrl)), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "gameSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static StackPane createRoot(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {

        StackPane root = new StackPane();
        Pane underPane = new Pane();
        underPane.getChildren().add((Node) board);
        Board gameBoard = (Board) board;
        gameBoard.setLayoutX(134);
        gameBoard.setLayoutY(155);

        Label bombs = new Label();
        Button pauseButton = new Button();
        pauseButton.getStyleClass().add("pausebutton");
        Button bombButton = new Button("Bomb");
        pauseButton.setOnMouseClicked(event -> gameCtrl.handlePause());
        bombButton.setOnMouseClicked(event -> gameCtrl.handleBombMode());

        Label timeLabelNode = (Label) timeLabel;
        Label scoreLabelNode = (Label) scoreLabel;
        Label progressLabelNode = (Label) progressLabel;
        HBox hBox = new HBox(10, timeLabelNode, scoreLabelNode, progressLabelNode);
        timeLabelNode.getStyleClass().add("game-label");
        scoreLabelNode.getStyleClass().add("game-label");
        progressLabelNode.getStyleClass().add("game-label");
        underPane.getChildren().addAll(hBox, bombButton, pauseButton);

        pauseButton.setLayoutX(10);
        pauseButton.setLayoutY(10);

        bombButton.setLayoutX(700);
        bombButton.setLayoutY(150);

        hBox.setLayoutX(200);
        hBox.setLayoutY(20);

        overlayPane = new StackPane();
        overlayPane.setAlignment(Pos.CENTER);
        overlayPane.setLayoutY(400);
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
