package app;

import controller.AudioCtrl;
import controller.GameCtrl;
import controller.LoginCtrl;
import controller.SceneCtrl;
import dao.UserDao;
import dao.impl.FileUserDao;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    public void start(Stage stage) {
        UserDao userDao = new FileUserDao();
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        GameCtrl gameCtrl = new GameCtrl(userDao, sceneCtrl);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, sceneCtrl, gameCtrl);
        AudioCtrl audioCtrl = new AudioCtrl("src/main/resources/Audio/bgmusic.mp3");
        audioCtrl.playBgMusic();
        loginCtrl.showInitialScene();
    }
}
