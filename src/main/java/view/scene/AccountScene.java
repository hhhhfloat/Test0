package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.LoginBox;

public class AccountScene {
    public static Scene accountScene() {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
