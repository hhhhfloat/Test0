package view;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class InformationUtil {
    public static void playInformation(Pane root, String info) {
        StackPane overlayPane = new StackPane();
        overlayPane.setMouseTransparent(true);
        root.getChildren().add(overlayPane);

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

