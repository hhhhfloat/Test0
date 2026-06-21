package modifying.auth.view.sceneRoots;

import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.view.box.LoginBox;

import java.nio.file.Paths;

public class LoginView extends StackPane{

    private final LoginBox loginBox;

    public LoginView(LoginCtrl loginCtrl){
        loginBox = new LoginBox(loginCtrl);
        getChildren().add(loginBox);
        getStylesheets().add(Paths.get("src","main","resources","css","SceneStyle","loginSceneStyle.css").toUri().toString());
    }

    public void clearTextField() {
        loginBox.clearTextField();
    }
}
