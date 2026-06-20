package modifying.auth.controller;


import javafx.stage.Stage;
import modifying.auth.view.scene.AccountScene;
import modifying.auth.view.scene.LoginScene;
import modifying.auth.view.scene.RegisterScene;

public class AuthSceneCtrl {
    private Stage authStage;

    /// Scenes without animation should be held
    private LoginScene loginScene;
    private AccountScene accountScene;
    private RegisterScene registerScene;

    public AuthSceneCtrl(Stage authStage){
        this.authStage = authStage;
    }

    public void showLoginScene(LoginCtrl loginCtrl) {
        if(loginScene == null){
            loginScene = new LoginScene(loginCtrl);
        }
        authStage.setScene(loginScene);
    }

    public void showAccountScene(LoginCtrl loginCtrl){
        if(accountScene == null){
            accountScene = new AccountScene(loginCtrl);
        }
        authStage.setScene(accountScene);
    }

    public void showRegisterScene(LoginCtrl loginCtrl){
        if(registerScene == null){
            registerScene = new RegisterScene(loginCtrl);
        }
        authStage.setScene(registerScene);
    }
}
