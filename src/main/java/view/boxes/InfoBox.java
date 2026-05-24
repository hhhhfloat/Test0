package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoBox extends HBox {
    private Label timeLabel;
    private Label scoreLabel;
    private Label progressLabel;
    public InfoBox(GameCtrl gameCtrl) {
        super(40);
        timeLabel = (Label) gameCtrl.getTimeLabel();
        scoreLabel = (Label) gameCtrl.getScoreLabel();
        progressLabel = (Label) gameCtrl.getProgressLabel();
        getChildren().addAll(timeLabel, scoreLabel, progressLabel);
    }
    public void freezeTime(boolean isFreeze){
        if(isFreeze)timeLabel.getStyleClass().add("freeze-style");
        else timeLabel.getStyleClass().remove("freeze-style");
    }
}
