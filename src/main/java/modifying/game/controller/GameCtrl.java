package modifying.game.controller;

import modifying.game.view.IGameScene;
import modifying.shared.controller.AudioCtrl;
import modifying.shared.model.MapSaveData;
import modifying.shared.model.TOAST_TYPE;

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
        // gameSceneCtrl.showToast("Launching" + gameId, TOAST_TYPE.ERROR);
        gameSceneCtrl.showConfirmDialog(
                "Launching" + gameId,
                "",
                ()->{

                },()->{}
        );
        // 创建游戏场景（后续由具体游戏类实现）
        // IGameScene gameScene = new SomeGameScene(this);
        // gameSceneCtrl.enterGameScene(gameScene);
    }

    // 提供获取场景控制器的方法（供游戏场景使用）
    public GameSceneCtrl getGameSceneCtrl() {
        return gameSceneCtrl;
    }

    // 在 GameCtrl.java 中添加
    public void openGameWindow(String gameId, double worldX, double worldY) {
        gameSceneCtrl.createGameWindow(gameId, worldX, worldY);
    }
    // ... 其他 getter
}