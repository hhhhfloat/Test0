package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.WinBox;
import java.nio.file.Paths;

public class WinScene extends Scene {
    public WinScene(GameCtrl gameCtrl) {
        super(new StackPane(new WinBox(gameCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "winSceneStyle.css").toUri().toString());
    }
}
