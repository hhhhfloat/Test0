package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.LevelBox;

public class LevelScene extends Scene{
    public LevelScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/levelSceneStyle.css").toExternalForm());
    }

    private static BorderPane createRoot(GameCtrl gameCtrl) {
        return new BorderPane(new LevelBox(gameCtrl));
    }
}
