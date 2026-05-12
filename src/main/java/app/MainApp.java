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

public class MainApp extends Application {
    public void start(Stage stage) {
        // dao初始化
        UserDao userDao = new FileUserDao();
        GameSaveDao gameSaveDao = new FileGameSaveDao();
        // controller初始化
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        AudioCtrl audioCtrl = new AudioCtrl();
        GameCtrl gameCtrl = new GameCtrl(userDao, sceneCtrl, audioCtrl, gameSaveDao);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, sceneCtrl, gameCtrl);
        // 行动
        audioCtrl.playBgMusic();
        loginCtrl.showInitialScene();
    }
}
