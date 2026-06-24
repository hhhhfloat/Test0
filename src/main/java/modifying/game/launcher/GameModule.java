package modifying.game.launcher;

import javafx.stage.Stage;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.game.view.tools.MouseGameEffect;
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
        MouseGameEffect.setAudioCtrl(audioCtrl);

        GameCtrl gameCtrl = new GameCtrl(gameSceneCtrl, audioCtrl, mapSaveData, username);

        gameSceneCtrl.showGameSelectScene(gameCtrl);

        primaryStage.show();
    }
}
