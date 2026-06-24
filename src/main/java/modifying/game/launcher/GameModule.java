package modifying.game.launcher;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.game.view.implement.GameSelectView;
import modifying.shared.controller.AudioCtrl;
import modifying.shared.controller.MainController;
import modifying.shared.model.MapSaveData;

public class GameModule{

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void start(Stage primaryStage, String username, MapSaveData mapSaveData) {
        GameSceneCtrl gameSceneCtrl = new GameSceneCtrl(primaryStage, mainController);

        AudioCtrl audioCtrl = new AudioCtrl();

        // 获取世界容器（用于摄像机）
        StackPane worldContainer = gameSceneCtrl.getContentContainer();
        // 从 GameSceneCtrl 常量构造摄像机
        CameraController camera = gameSceneCtrl.getCameraController();

        GameCtrl gameCtrl = new GameCtrl(gameSceneCtrl, audioCtrl, mapSaveData, username);

        GameSelectView selectView = new GameSelectView(gameCtrl,camera);
        gameSceneCtrl.pushScene(selectView);


        primaryStage.show();
    }
}
