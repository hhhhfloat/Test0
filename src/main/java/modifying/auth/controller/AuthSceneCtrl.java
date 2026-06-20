package modifying.auth.controller;


import javafx.stage.Stage;
import modifying.auth.view.scene.AccountScene;
import modifying.auth.view.scene.LoginScene;

public class AuthSceneCtrl {
    private Stage authStage;

    /// Scenes without animation should be held
    private LoginScene loginScene;
    private AccountScene accountScene;

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
}
