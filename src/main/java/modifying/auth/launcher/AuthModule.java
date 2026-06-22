package modifying.auth.launcher;

import before.controller.AudioCtrl;
import before.dao.UserDao;
import before.dao.impl.FileUserDao;
import modifying.auth.view.tools.MouseGlowEffect;
import javafx.application.Application;
import javafx.stage.Stage;
import modifying.auth.controller.AuthSceneCtrl;
import modifying.auth.controller.LoginCtrl;

public class AuthModule extends Application{



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

        authStage.show();

    }
}