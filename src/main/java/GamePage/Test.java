package GamePage;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;


public class Test extends Application {
    private int counter = 0;
    private Label label;
    public void start(Stage stage) {
        Timeline timeline;
        label = new Label("0");
        VBox root = new VBox(10, label);
        stage.setScene(new Scene(root, 100, 100));
        stage.show();
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    counter++;
                    label.setText(String.valueOf(counter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
