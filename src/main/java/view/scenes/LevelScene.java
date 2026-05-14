package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.LevelBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LevelScene extends Scene{
    public LevelScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "levelSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static BorderPane createRoot(GameCtrl gameCtrl) {
        return new BorderPane(new LevelBox(gameCtrl));
    }
}
