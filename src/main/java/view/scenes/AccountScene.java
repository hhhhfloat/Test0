package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import model.entity.Account;
import view.boxes.AccountBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AccountScene  extends Scene{
    public AccountScene(Account account, GameCtrl gameCtrl) {
        super(createRoot(account, gameCtrl), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "accountSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static StackPane createRoot(Account account, GameCtrl gameCtrl) {
        return new StackPane(new AccountBox(gameCtrl, account));
    }
}
