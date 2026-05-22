package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.AccountBox;
import java.nio.file.Paths;

public class AccountScene  extends Scene{
    public AccountScene(LoginCtrl loginCtrl) {
        super(new StackPane(new AccountBox(loginCtrl)), 800, 800);
        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
    }
}
