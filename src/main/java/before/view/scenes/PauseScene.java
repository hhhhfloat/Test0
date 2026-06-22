package before.view.scenes;

import before.controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.view.tools.MouseGlowEffect;
import before.view.boxes.PauseBox;
import java.nio.file.Paths;

public class PauseScene extends Scene{
    public PauseScene(GameCtrl gameCtrl) {
        super(new StackPane(new PauseBox(gameCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "pauseSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
