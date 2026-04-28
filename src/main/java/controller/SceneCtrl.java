package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.scenes.*;

public class SceneCtrl {
    private final Stage primaryStage;
    public SceneCtrl(Stage stage) {
        primaryStage = stage;
    }

    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
