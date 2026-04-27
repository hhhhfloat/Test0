package GamePage;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class TimerLabel {
    int counter = 0;
    Timeline timeline;

    public Label timer() {
        Label timer = new Label();
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    counter++;
                    timer.setText(String.valueOf(counter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return timer;
    }
}
