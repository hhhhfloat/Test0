package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.RegisterBox;

public class RegisterScene extends Scene{
    public RegisterScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/registerSceneStyle.css").toExternalForm());
    }
    public static BorderPane createRoot(LoginCtrl loginCtrl) {
        RegisterBox registerBox = new RegisterBox(loginCtrl);
        BorderPane borderPane = new BorderPane(registerBox);
        return borderPane;
    }
}
