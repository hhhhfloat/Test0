package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class GameScene extends Scene{
    public GameScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/gameSceneStyle.css").toExternalForm());
    }

    public static BorderPane createRoot(GameCtrl gameCtrl) {
        BorderPane root = new BorderPane();
        return root;
    }
}
