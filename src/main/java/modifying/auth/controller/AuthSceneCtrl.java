package modifying.auth.controller;


import before.view.MouseGlowEffect;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modifying.auth.view.sceneRoots.AccountView;
import modifying.auth.view.sceneRoots.LoginView;
import modifying.auth.view.sceneRoots.RegisterView;

import java.util.HashMap;
import java.util.Map;

public class AuthSceneCtrl {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;
    public static int getScreenWidth(){
        return SCREEN_WIDTH;
    }
    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    private Stage authStage;
    private Scene authScene;
    private final Map<String, Parent> viewCache = new HashMap<>();

    public Scene getAuthScene() {
        return authScene;
    }

    /// Scenes without animation should be held
    private LoginView loginView;
    private AccountView accountView;
    private RegisterView registerView;

    private StackPane currentRoot;

    public AuthSceneCtrl(Stage authStage){
        this.authStage = authStage;

        // empty stackPane temp
        this.authScene = new Scene(new StackPane(), SCREEN_WIDTH,SCREEN_HEIGHT);

        authStage.setScene(authScene);
    }

    public void showLoginScene(LoginCtrl loginCtrl) {
        loginView = (LoginView) viewCache.computeIfAbsent("LOGIN", k -> new LoginView(loginCtrl));

        loginView.clearTextField();

        MouseGlowEffect.attach(authScene, loginView);

        authScene.setRoot(loginView);
        currentRoot = loginView;
    }

    public void showAccountScene(LoginCtrl loginCtrl){
        accountView = (AccountView) viewCache.computeIfAbsent("ACCOUNT", k-> new AccountView(loginCtrl));
        accountView.syncUser();
        MouseGlowEffect.attach(authScene, accountView);
        authScene.setRoot(accountView);
        currentRoot = accountView;
    }

    public void showRegisterScene(LoginCtrl loginCtrl){
        registerView = (RegisterView) viewCache.computeIfAbsent("REGISTER", k-> new RegisterView(loginCtrl));
        registerView.clearTextField();
        MouseGlowEffect.attach(authScene, accountView);
        authScene.setRoot(registerView);
        currentRoot = registerView;
    }
}
