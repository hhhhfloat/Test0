package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.MouseGlowEffect;
import view.boxes.InitialBox;
import java.nio.file.Paths;

public class InitialScene extends Scene {
    public InitialScene(LoginCtrl loginCtrl) {
        super(new StackPane(new InitialBox(loginCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "initialSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
