package app;

import controller.AudioCtrl;
import controller.GameCtrl;
import controller.LoginCtrl;
import controller.SceneCtrl;
import dao.GameSaveDao;
import dao.UserDao;
import dao.impl.FileGameSaveDao;
import dao.impl.FileUserDao;
import javafx.application.Application;
import javafx.stage.Stage;
import view.scenes.GameScene;

public class MainApp extends Application {
    public void start(Stage stage) {
        // dao初始化
        UserDao userDao = new FileUserDao();
        GameSaveDao gameSaveDao = new FileGameSaveDao();
        // controller初始化
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        GameCtrl gameCtrl = new GameCtrl(userDao, sceneCtrl, gameSaveDao);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, sceneCtrl, gameCtrl);
        AudioCtrl audioCtrl = new AudioCtrl("src/main/resources/Audio/bgmusic.mp3");
        // 行动
        audioCtrl.playBgMusic();
        loginCtrl.showInitialScene();
    }
}
