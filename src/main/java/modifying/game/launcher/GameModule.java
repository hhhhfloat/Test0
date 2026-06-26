package modifying.game.launcher;

import javafx.stage.Stage;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.shared.controller.AudioCtrl;
import modifying.shared.controller.MainController;
import modifying.shared.model.MapSaveData;

public class GameModule {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void start(Stage primaryStage, String username, MapSaveData mapSaveData) {
        // 1. 创建场景控制器
        GameSceneCtrl gameSceneCtrl = new GameSceneCtrl(primaryStage, mainController);

        // 2. 创建音频和游戏控制器
        AudioCtrl audioCtrl = new AudioCtrl();
        GameCtrl gameCtrl = new GameCtrl(gameSceneCtrl, audioCtrl, mapSaveData, username);

        // 3. 让 GameSceneCtrl 显示大厅（它内部会创建 GameSelectView）
        gameSceneCtrl.showGameSelectView(gameCtrl);

        primaryStage.show();
    }
}