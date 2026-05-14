package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.RegisterBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterScene extends Scene{
    public RegisterScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "registerSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }
    public static BorderPane createRoot(LoginCtrl loginCtrl) {
        RegisterBox registerBox = new RegisterBox(loginCtrl);
        return new BorderPane(registerBox);
    }
}
