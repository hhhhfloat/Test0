package before.view.scenes;

import before.controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.view.tools.MouseGlowEffect;
import before.view.boxes.LoadBox;
import java.nio.file.Paths;

public class LoadScene extends Scene{
    public LoadScene(LoginCtrl loginCtrl) {
        super(new StackPane(new LoadBox(loginCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "loadSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
