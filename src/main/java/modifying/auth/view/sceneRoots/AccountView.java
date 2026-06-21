package modifying.auth.view.sceneRoots;

import javafx.scene.layout.StackPane;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.view.box.AccountBox;

import java.nio.file.Paths;

public class AccountView extends StackPane {

    private AccountBox accountBox;

    public AccountView(LoginCtrl loginCtrl){
        accountBox = new AccountBox(loginCtrl);
        getChildren().add(accountBox);
        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
    }

    public void syncUser(){
        accountBox.syncAccount();
    }

}
