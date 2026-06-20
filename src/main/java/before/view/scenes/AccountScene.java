package before.view.scenes;

import before.controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import before.view.MouseGlowEffect;
import before.view.boxes.AccountBox;
import java.nio.file.Paths;

public class AccountScene  extends Scene{
    public AccountScene(LoginCtrl loginCtrl) {
        super(new StackPane(new AccountBox(loginCtrl)), 800, 800);
        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
