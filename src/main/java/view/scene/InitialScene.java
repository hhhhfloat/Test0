package view.scene;

import javafx.scene.Scene;
import view.scene.Boxes.InitialBtnBox;

public class InitialScene {
    public static Scene initialScene() {
        Scene scene = new Scene(InitialBtnBox.initialBtnBox());
        return scene;
    }
}
