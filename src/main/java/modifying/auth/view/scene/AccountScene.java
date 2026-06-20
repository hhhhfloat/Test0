package modifying.auth.view.scene;

import before.view.MouseGlowEffect;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.launcher.AuthModule;
import modifying.auth.view.box.AccountBox;

import java.nio.file.Paths;

public class AccountScene extends Scene {

    private static final int SCREEN_WIDTH = AuthModule.getScreenWidth();
    private static final int SCREEN_HEIGHT = AuthModule.getScreenHeight();

    private static StackPane root;
    private AccountBox accountBox;

    public AccountScene(LoginCtrl loginCtrl){
        super(new StackPane(createRoot()), SCREEN_WIDTH,SCREEN_HEIGHT);

        accountBox = new AccountBox(loginCtrl);
        root.getChildren().add(accountBox);

        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }

    public static StackPane createRoot(){
        root = new StackPane();
        return root;
    }

    public void syncUser(){
        accountBox.syncAccount();
    }

}
