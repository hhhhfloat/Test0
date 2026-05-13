package view.scenes;


import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import view.boxes.InitialBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class InitialScene extends Scene{
    public InitialScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 800);
        Path cssPath = Paths.get("src","main","resources","css","initialSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }
    private static StackPane createRoot(LoginCtrl loginCtrl) {
        return new StackPane(new InitialBox(loginCtrl));
    }
}
