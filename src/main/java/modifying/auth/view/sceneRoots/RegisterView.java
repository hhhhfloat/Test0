package modifying.auth.view.sceneRoots;

import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.view.box.RegisterBox;

import java.nio.file.Paths;

public class RegisterView extends StackPane {

    private RegisterBox registerBox;

    public RegisterView(LoginCtrl loginCtrl){

        registerBox = new RegisterBox(loginCtrl);
        getChildren().add(registerBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "registerSceneStyle.css").toUri().toString());
    }

    public void clearTextField(){
        registerBox.clearTextField();
    }

}
