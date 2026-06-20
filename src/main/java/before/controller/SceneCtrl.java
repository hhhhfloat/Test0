package before.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneCtrl {
    private final Stage primaryStage;
    public SceneCtrl(Stage stage) {
        primaryStage = stage;
    }
    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }
    public Pane getRoot() {
        return (Pane) primaryStage.getScene().getRoot();
    }
}
