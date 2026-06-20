package modifying.auth.view.scene;

import before.view.MouseGlowEffect;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.launcher.AuthModule;
import modifying.auth.view.box.RegisterBox;

import java.nio.file.Paths;

public class RegisterScene extends Scene {

    private static final int SCREEN_WIDTH = AuthModule.getScreenWidth();
    private static final int SCREEN_HEIGHT = AuthModule.getScreenHeight();

    public RegisterScene(LoginCtrl loginCtrl){
        super(new StackPane(new RegisterBox(loginCtrl)),800,800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "registerSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
}
