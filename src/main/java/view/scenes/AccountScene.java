package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import model.entity.Account;
import view.boxes.AccountBox;
import java.nio.file.Paths;

public class AccountScene  extends Scene{
    public AccountScene(Account account, GameCtrl gameCtrl) {
        super(new StackPane(new AccountBox(gameCtrl, account)), 800, 800);
        getStylesheets().add( Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css").toUri().toString());
    }
}
