package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.LevelBtnBox;

public class LevelScene {
    public static Scene levelScene() {
        BorderPane borderPane = new BorderPane(LevelBtnBox.levelBtnBox());
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
