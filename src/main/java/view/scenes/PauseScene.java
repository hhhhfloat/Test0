package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.PauseBox;
import java.nio.file.Paths;

public class PauseScene extends Scene{
    public PauseScene(GameCtrl gameCtrl) {
        super(new BorderPane(new PauseBox(gameCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "pauseSceneStyle.css").toString());
    }
}
