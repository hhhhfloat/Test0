package GamePage.Labels;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TimerLabel {
    int counter = 0;
    Timeline timeline;

    public Label timer() {
        Label timer = new Label();
        timer.setText("Time Already Used: 0");
        timer.setFont(Font.font(30));
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    counter++;
                    timer.setText("Time Already Used: "+ String.valueOf(counter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return timer;
    }
}
