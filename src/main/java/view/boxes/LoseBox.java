package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LoseBox extends VBox {
    public LoseBox(GameCtrl gameCtrl) {
        super(10);
        getStyleClass().add("losebox");
        Label state = new Label("Lose..."),
                scoreLabel = new Label("Score: " + gameCtrl.getScoreLabel().getScore()),
                timeLabel = new Label("Time used: " + gameCtrl.getTimeLabel().getTime()),
                progressLabel = new Label("Managed to eliminate: " + gameCtrl.getProgressLabel().getEliminated() + "/" + gameCtrl.getProgressLabel().getTotal());
        state.getStyleClass().add("state");
        scoreLabel.getStyleClass().add("stat");
        timeLabel.getStyleClass().add("stat");
        progressLabel.getStyleClass().add("stat");

        Button confirm = new Button("OH NO!!!");
        confirm.setOnMouseClicked(event -> gameCtrl.showLevelSelectScene());
        getChildren().addAll(state, scoreLabel, timeLabel, progressLabel, confirm);
    }
}
