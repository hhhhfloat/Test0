package before.view.boxes;

import before.controller.GameCtrl;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WinBox extends VBox {

    private Animation colorCycle;
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
        List<Node> list = new ArrayList<>(Arrays.asList(state,scoreLabel,timeLabel,confirm));

        getChildren().addAll(list);


    }



}
