package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.PauseBox;

public class PauseScene extends Scene{
    public PauseScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/SceneStyle/pauseSceneStyle.css").toExternalForm());
    }

    public static BorderPane createRoot(GameCtrl gameCtrl) {
        PauseBox pauseBox = new PauseBox(gameCtrl);
        return new BorderPane(pauseBox);
    }
}
