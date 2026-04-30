package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import model.entity.Account;
import view.boxes.AccountBox;

public class AccountScene  extends Scene{
    public AccountScene(GameCtrl gameCtrl) {
        super(createRoot(gameCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/accountSceneStyle.css").toExternalForm());
    }


    public AccountScene(Account account, GameCtrl gameCtrl) {
        super(createRoot(account, gameCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/accountSceneStyle.css").toExternalForm());
    }

    private static StackPane createRoot(GameCtrl gameCtrl) {
        return new StackPane(new AccountBox(gameCtrl));
    }

    private static StackPane createRoot(Account account, GameCtrl gameCtrl) {
        return new StackPane(new AccountBox(account, gameCtrl));
    }
}
