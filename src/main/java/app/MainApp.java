package app;

import controller.GameCtrl;
import controller.LoginCtrl;
import controller.SceneCtrl;
import dao.UserDao;
import dao.impl.FileUserDao;
import javafx.application.Application;
import javafx.stage.Stage;
import view.common.AudioUtil;

public class MainApp extends Application {


    public void start(Stage stage) {
        AudioUtil.playMusic();
        UserDao userDao = new FileUserDao();
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        GameCtrl gameCtrl = new GameCtrl(userDao, sceneCtrl);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, sceneCtrl);
        loginCtrl.showInitialScene();
    }
}
