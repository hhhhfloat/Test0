package view.game_nodes.Labels;

import controller.GameCtrl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import view.game_nodes.Interfaces.TimeLabelInterface;

public class TimeLabel extends Label implements TimeLabelInterface {
    private Timeline timeline;
    private int remainingTime;
    private final GameCtrl gameCtrl;
    private final int totalTime;
    public TimeLabel(int time, GameCtrl gameCtrl) {
        super();
        remainingTime = time;
        totalTime = time;
        this.gameCtrl = gameCtrl;
        setTime(remainingTime);
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getTime() {
        return totalTime - remainingTime;
    }

    @Override
    public void start() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    if (remainingTime > 0) {
                        remainingTime--;
                        setTime(remainingTime);
                    } else {
                        timeline.stop();
                        gameCtrl.timeUp();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void pauseTime() {
        timeline.pause();
    }

    @Override
    public void pauseTime(int milliseconds){
        pauseTime();
        try {
            wait(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        continueTime();
    }

    @Override
    public void continueTime() {
        timeline.play();
    }

    private void setTime(int time) {
        setText(String.format("Time remaining: %d : %02d",time/60, time%60));
    }
}
