package modifying.auth.view.scene;

import before.view.MouseGlowEffect;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.launcher.AuthModule;
import modifying.auth.view.box.AccountBox;

import java.nio.file.Paths;

public class AccountScene extends Scene {

    private static final int SCREEN_WIDTH = AuthModule.getScreenWidth();
    private static final int SCREEN_HEIGHT = AuthModule.getScreenHeight();

    public AccountScene(LoginCtrl loginCtrl){
        super(new StackPane(new AccountBox(loginCtrl)), SCREEN_WIDTH,SCREEN_HEIGHT);
        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
