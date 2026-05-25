package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WinBox extends VBox {
    public WinBox(GameCtrl gameCtrl) {
        super(10);
        getStyleClass().add("losebox");
        Label state = new Label("VICTORY"),
                scoreLabel = new Label("Score: " + gameCtrl.getScoreLabel().getScore()),
                timeLabel = new Label("Time used: " + gameCtrl.getTimeLabel().getTime());
        state.getStyleClass().add("state");
        scoreLabel.getStyleClass().add("stat");
        timeLabel.getStyleClass().add("stat");
        Button confirm = new Button("Hooray!!!");
        confirm.setOnMouseClicked(event -> gameCtrl.showLevelSelectScene());
        getChildren().addAll(state, scoreLabel, timeLabel,confirm);
    }
}
