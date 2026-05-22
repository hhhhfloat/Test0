package view;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class InformationUtil{
    public static void playInformation(Pane root, String info) {
        Path cssPath = Paths.get("src", "main", "resources", "css", "infoStyle.css");
        String cssUri = cssPath.toUri().toString();
        root.getStylesheets().add(cssUri);

        StackPane overlayPane = new StackPane();
        overlayPane.setMouseTransparent(true);
        root.getChildren().add(overlayPane);

        Label information = new Label(info);
        overlayPane.getChildren().add(information);

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

