package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.LoadBox;
import java.nio.file.Paths;

public class LoadScene extends Scene{
    public LoadScene(GameCtrl gameCtrl) {
        super(new StackPane(new LoadBox(gameCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "loadSceneStyle.css").toUri().toString());
    }
}
