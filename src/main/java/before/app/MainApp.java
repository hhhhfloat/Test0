package before.app;

import modifying.shared.controller.AudioCtrl;
import before.controller.LoginCtrl;
import before.controller.SceneCtrl;
import modifying.auth.dao.UserDao;
import modifying.auth.dao.implement.FileUserDao;
import javafx.application.Application;
import javafx.stage.Stage;
import modifying.auth.view.tools.MouseGlowEffect;

public class MainApp extends Application {
    public void start(Stage stage) {
        // dao初始化
        UserDao userDao = new FileUserDao();
        // controller初始化
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        AudioCtrl audioCtrl = new AudioCtrl();
        MouseGlowEffect.setAudioCtrl(audioCtrl);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, audioCtrl, sceneCtrl);
        // 行动
        // audioCtrl.playBgMusic();
        loginCtrl.showInitialScene();
        stage.show();
        // Platform.runLater(()->new SimpleFpsHUD(stage));

    }
}
