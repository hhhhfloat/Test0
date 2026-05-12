package view.scenes;

import controller.GameCtrl;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import view.game_nodes.Interfaces.BoardInterface;
import view.game_nodes.Interfaces.ProgressLabelInterface;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;

public class GameScene extends Scene{
    public GameScene(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {
        super(new BorderPane(createRoot(board, timeLabel, scoreLabel, progressLabel, gameCtrl)),800,800);
    }

    private static BorderPane createRoot(BoardInterface board, TimeLabelInterface timeLabel, ScoreLabelInterface scoreLabel, ProgressLabelInterface progressLabel, GameCtrl gameCtrl) {
        Button pauseButton = new Button("Pause");
        pauseButton.setOnMouseClicked(event -> gameCtrl.handlePause());
        BorderPane root = new BorderPane((Node)board);
        HBox hBox = new HBox(10, (Node)timeLabel, (Node)scoreLabel);
        root.setLeft(pauseButton);
        root.setTop(hBox);
        return root;
    }
}
