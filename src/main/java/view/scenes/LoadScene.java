package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.boxes.LoadBox;

public class LoadScene extends Scene{
    public LoadScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 600);
    }
    public static BorderPane createRoot(GameCtrl gameCtrl) {
        LoadBox loadBox = new LoadBox(gameCtrl);
        BorderPane borderPane = new BorderPane(loadBox);
        return borderPane;
    }
}
