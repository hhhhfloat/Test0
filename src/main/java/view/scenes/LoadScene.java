package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.LoadBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadScene extends Scene{
    public LoadScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "loadSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }
    public static BorderPane createRoot(GameCtrl gameCtrl) {
        return new BorderPane(new LoadBox(gameCtrl));
    }
}
