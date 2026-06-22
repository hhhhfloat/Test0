package before.view.scenes;

import before.controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.view.tools.MouseGlowEffect;
import before.view.boxes.RegisterBox;
import java.nio.file.Paths;

public class RegisterScene extends Scene{
    public RegisterScene(LoginCtrl loginCtrl) {
        super(new StackPane(new RegisterBox(loginCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "registerSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
