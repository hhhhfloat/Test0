package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import model.entity.Account;
import view.boxes.AccountBox;

public class AccountScene  extends Scene{
    public AccountScene(Account account, boolean isTourist, LoginCtrl loginCtrl) {
        super(createRoot(account, isTourist, loginCtrl), 800, 600);
    }

    private static StackPane createRoot(Account account, boolean isTourist, LoginCtrl loginCtrl) {
        StackPane root = new StackPane();
        //root.setBackground(BgImage.getBgImage());

        AccountBox accountBox = new AccountBox(account, isTourist, loginCtrl);
        root.getChildren().add(accountBox);

        return root;
    }
}
