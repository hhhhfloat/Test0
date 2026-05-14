package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.PauseBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PauseScene extends Scene{
    public PauseScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "pauseSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    public static BorderPane createRoot(GameCtrl gameCtrl) {
        PauseBox pauseBox = new PauseBox(gameCtrl);
        return new BorderPane(pauseBox);
    }
}
