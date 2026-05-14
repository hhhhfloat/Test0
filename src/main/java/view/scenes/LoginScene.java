package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.LoginBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginScene extends Scene {
    public LoginScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "loginSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static StackPane createRoot(LoginCtrl loginCtrl) {
        return new StackPane(new LoginBox(loginCtrl));
    }
}
