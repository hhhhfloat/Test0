package modifying.auth.view.scene;

import before.view.MouseGlowEffect;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.controller.AuthSceneCtrl;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.launcher.AuthModule;
import modifying.auth.view.box.LoginBox;

import java.nio.file.Paths;

public class LoginScene extends Scene {

    private static final int SCREEN_WIDTH = AuthModule.getScreenWidth();
    private static final int SCREEN_HEIGHT = AuthModule.getScreenHeight();

    private static StackPane root;
    private LoginBox loginBox;

    public LoginScene(LoginCtrl loginCtrl){
        super(new StackPane(createRoot()), SCREEN_WIDTH, SCREEN_HEIGHT);

        loginBox = new LoginBox(loginCtrl);
        root.getChildren().add(loginBox);

        getStylesheets().add(Paths.get("src","main","resources","css","SceneStyle","loginSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane)getRoot());
    }

    private static StackPane createRoot(){
        root = new StackPane();
        return root;
    }


    public void clearTextField() {
        loginBox.clearTextField();
    }
}
