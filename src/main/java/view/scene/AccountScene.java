package view.scene;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.AccountBtnBox;

public class AccountScene {
    public static Scene accountScene() {
        BorderPane borderPane = new BorderPane(AccountBtnBox.accountBtnBox());
        Scene scene = new Scene(borderPane);
        return scene;
    }
}
