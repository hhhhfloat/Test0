package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.MouseGlowEffect;
import view.boxes.LoseBox;
import java.nio.file.Paths;

public class LoseScene extends Scene {
    public LoseScene(GameCtrl gameCtrl) {
        super(new StackPane(new LoseBox(gameCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "loseSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
