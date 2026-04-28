package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.LoginBox;

public class LoginScene {
    public static Scene loginScene() {

        BorderPane borderPane = new BorderPane(LoginBox.loginBox());
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
