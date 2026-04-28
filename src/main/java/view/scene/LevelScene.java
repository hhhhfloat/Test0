package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.LevelBox;

public class LevelScene {
    public static Scene levelScene() {
        BorderPane borderPane = new BorderPane(LevelBox.levelBtnBox());
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
