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
import view.game_nodes.Interfaces.BoardInterface;
import view.game_nodes.Interfaces.ProgressLabelInterface;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;

public class GameScene extends Scene{
    public static StackPane overlayPane;
    public GameScene(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {
        super(new BorderPane(createRoot(board, timeLabel, scoreLabel, progressLabel, gameCtrl)),800,800);
    }

    private static Pane createRoot(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {

        StackPane root = new StackPane();
        BorderPane underPane = new BorderPane((Node) board);
        Button pauseButton = new Button("Pause");
        pauseButton.setOnMouseClicked(event -> gameCtrl.handlePause());
        HBox hBox = new HBox(10, (Node)timeLabel, (Node)scoreLabel);
        underPane.setLeft(pauseButton);
        underPane.setTop(hBox);

        overlayPane = new StackPane();
        overlayPane.setAlignment(Pos.CENTER);
        overlayPane.setMouseTransparent(true);
        root.getChildren().addAll(underPane, overlayPane);

        return root;
    }

    public static void playInformation(String info) {
        Label information = new Label(info);

        information.setStyle(
                "-fx-text-fill: red; " +
                        "-fx-background-color: rgba(0,0,0,0); " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-radius: 10px;"
        );
        information.setAlignment(Pos.CENTER);
        overlayPane.getChildren().add(information);
        StackPane.setAlignment(information, Pos.CENTER);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(200), information);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), information);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), information);
        translateTransition.setFromY(-300);
        translateTransition.setToY(-350);

        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(200), information);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setDelay(Duration.seconds(0.5));

        ParallelTransition parallelTransition = new ParallelTransition(fadeInTransition, scaleTransition, translateTransition);
        SequentialTransition sequentialTransition = new SequentialTransition(information, parallelTransition, fadeOutTransition);

        sequentialTransition.setOnFinished(e -> overlayPane.getChildren().remove(information));

        sequentialTransition.play();
    }
}
