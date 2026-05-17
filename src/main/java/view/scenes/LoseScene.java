package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import javafx.scene.layout.VBox;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoseScene extends Scene {
    public LoseScene(GameCtrl gameCtrl, ScoreLabelInterface scoreLabelInterface, TimeLabelInterface timeLabelInterface) {
        super(createRoot(gameCtrl, scoreLabelInterface, timeLabelInterface), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "loseSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }
    public static StackPane createRoot(GameCtrl gameCtrl, ScoreLabelInterface scoreLabelInterface, TimeLabelInterface timeLabelInterface) {
        Label state = new Label("Lose...");
        state.getStyleClass().add("state");
        Label scoreLabel = new Label("Score: " + scoreLabelInterface.getScore());
        scoreLabel.getStyleClass().add("stat");
        Label timeLabel = new Label("Time used: " + timeLabelInterface.getTime());
        timeLabel.getStyleClass().add("stat");
        Label progressLabel = new Label("Managed to eliminate"+ "/");
        Button confirm = new Button("OH NO!!!");
        confirm.setOnMouseClicked(event -> gameCtrl.showAccountScene());
        VBox vBox = new VBox(40,state, scoreLabel, timeLabel,confirm);
        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);
        return root;
    }
}
