package modifying.shared.controller;

import javafx.stage.Stage;
import modifying.auth.launcher.AuthModule;
import modifying.game.launcher.GameModule;
import modifying.shared.model.MapSaveData;

public class MainController {

    private Stage primaryStage;
    private AuthModule authModule;
    private GameModule gameModule;
    private MapSaveData mapSaveData;
    private String currentUsername;

    /**
     * 由 MainApp 调用，传入主 Stage
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        runAuthModule();
    }

    /**
     * Auth 模块登录成功后调用
     */
    public void onGameStart(String username) {
        this.currentUsername = username;
        // 关闭认证窗口
        primaryStage.close();
        // 解除引用，让回收器自行销毁
        if(authModule != null){
            authModule = null;
        }

        // 创建游戏窗口
        Stage gameStage = new Stage();
        gameModule = new GameModule();
        // 启动游戏模块，传入用户名和存档数据
        gameModule.setMainController(this);
        gameModule.start(gameStage, username, mapSaveData);
    }

    /**
     * 由 LoginCtrl 在加载存档后设置
     */
    public void setMapSaveData(MapSaveData mapSaveData) {
        this.mapSaveData = mapSaveData;
    }

    private void runAuthModule() {
        authModule = new AuthModule();
        authModule.setMain(this);

        authModule.start(primaryStage);
    }
}