package controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.scenes.*;

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
    public Stage getPrimaryStage() {
        return primaryStage;
    }

//    private AccountScene accountScene;
//    private GameScene gameScene;
//    private InitialScene initialScene;
//    private LevelSelectScene levelSelectScene;
//    private LoadScene loadScene;
//    private LoginScene loginScene;
//    private LoseScene loseScene;
//    private PauseScene pauseScene;
//    private RegisterScene registerScene;
//    private WinScene winScene;
//
//    public InitialScene newInitialScene(LoginCtrl loginCtrl){
//        if (initialScene == null) {
//            initialScene = new InitialScene(loginCtrl);
//        }
//        return initialScene;
//    }

}
