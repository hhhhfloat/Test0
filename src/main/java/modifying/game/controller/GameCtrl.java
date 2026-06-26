package modifying.game.controller;

import modifying.game.view.IGameScene;
import modifying.shared.controller.AudioCtrl;
import modifying.shared.model.MapSaveData;

public class GameCtrl {

    private final GameSceneCtrl gameSceneCtrl;
    private final AudioCtrl audioCtrl;
    private final String username;
    private final MapSaveData mapSaveData;

    public GameCtrl(GameSceneCtrl gameSceneCtrl, AudioCtrl audioCtrl, MapSaveData mapSaveData, String username) {
        this.gameSceneCtrl = gameSceneCtrl;
        this.audioCtrl = audioCtrl;
        this.username = username;
        this.mapSaveData = mapSaveData;
    }

    public void launchGame(String gameId) {
        System.out.println("Launching " + gameId);
        // 创建游戏场景（后续由具体游戏类实现）
        // IGameScene gameScene = new SomeGameScene(this);
        // gameSceneCtrl.enterGameScene(gameScene);
    }

    // 提供获取场景控制器的方法（供游戏场景使用）
    public GameSceneCtrl getGameSceneCtrl() {
        return gameSceneCtrl;
    }

    // ... 其他 getter
}