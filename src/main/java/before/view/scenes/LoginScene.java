package before.view.scenes;

import before.controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import before.view.MouseGlowEffect;
import before.view.boxes.LoginBox;
import java.nio.file.Paths;

public class LoginScene extends Scene {
    public LoginScene(LoginCtrl loginCtrl) {
        super(new StackPane(new LoginBox(loginCtrl)), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "loginSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
