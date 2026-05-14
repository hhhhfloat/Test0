package app;

import controller.*;
import dao.GameSaveDao;
import dao.UserDao;
import dao.impl.FileGameSaveDao;
import dao.impl.FileUserDao;
import javafx.application.Application;
import javafx.application.Platform;
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
        LoginCtrl loginCtrl = new LoginCtrl(userDao, audioCtrl, sceneCtrl, gameCtrl);
        // 行动
        audioCtrl.playBgMusic();
        loginCtrl.showInitialScene();
        stage.show();
        Platform.runLater(()->new SimpleFpsHUD(stage));
    }
}
