package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.RegisterBox;

public class RegisterScene {
    public static Scene registerScene() {
        BorderPane borderPane = new BorderPane(RegisterBox.registerBox());
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
