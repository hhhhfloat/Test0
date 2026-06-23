package modifying.auth.launcher;

import modifying.shared.controller.AudioCtrl;
import modifying.auth.dao.UserDao;
import modifying.auth.dao.implement.FileUserDao;
import modifying.auth.view.tools.MouseGlowEffect;
import javafx.stage.Stage;
import modifying.auth.controller.AuthSceneCtrl;
import modifying.auth.controller.LoginCtrl;
import modifying.shared.controller.MainController;

public class AuthModule{

    private MainController mainController;
    public void setMain(MainController mainController){
        this.mainController = mainController;
    }

    public void start(Stage authStage){
        UserDao userDao = new FileUserDao();

        AuthSceneCtrl authSceneCtrl = new AuthSceneCtrl(authStage, mainController);

        ///  These two don't need to be separated by auth and game
        AudioCtrl audioCtrl = new AudioCtrl();
        MouseGlowEffect.setAudioCtrl(audioCtrl);

        LoginCtrl loginCtrl = new LoginCtrl(userDao, audioCtrl, authSceneCtrl, mainController);

        // audioCtrl.playBgMusic();

        authSceneCtrl.showLoginScene(loginCtrl);

        authStage.show();


    }
}