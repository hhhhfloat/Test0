package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.LoginBox;

public class LoginScene extends Scene {
    public LoginScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/loginSceneStyle.css").toExternalForm());
    }

    private static StackPane createRoot(LoginCtrl loginCtrl) {
        StackPane root = new StackPane();
        root.getChildren().add(new LoginBox(loginCtrl));
        return root;
    }
}
