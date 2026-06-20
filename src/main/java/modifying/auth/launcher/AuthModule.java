package modifying.auth.launcher;

import before.controller.AudioCtrl;
import before.dao.UserDao;
import before.dao.impl.FileUserDao;
import before.view.MouseGlowEffect;
import javafx.application.Application;
import javafx.stage.Stage;
import modifying.auth.controller.AuthSceneCtrl;
import modifying.auth.controller.LoginCtrl;

public class AuthModule extends Application{

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;
    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }
    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    @Override
    public void start(Stage authStage){
        UserDao userDao = new FileUserDao();

        AuthSceneCtrl authSceneCtrl = new AuthSceneCtrl(authStage);

        ///  These two don't need to be separated by auth and game
        AudioCtrl audioCtrl = new AudioCtrl();
        MouseGlowEffect.setAudioCtrl(audioCtrl);

        LoginCtrl loginCtrl = new LoginCtrl(userDao, audioCtrl, authSceneCtrl);

        // audioCtrl.playBgMusic();

        authSceneCtrl.showLoginScene(loginCtrl);



    }
}