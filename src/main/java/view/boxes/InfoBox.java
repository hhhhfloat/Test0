package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoBox extends HBox {
    public InfoBox(GameCtrl gameCtrl) {
        super(40);
        Label timeLabel = (Label) gameCtrl.getTimeLabel();
        Label scoreLabel = (Label) gameCtrl.getScoreLabel();
        Label progressLabel = (Label) gameCtrl.getProgressLabel();

        getChildren().addAll(timeLabel, scoreLabel, progressLabel);
    }
}
