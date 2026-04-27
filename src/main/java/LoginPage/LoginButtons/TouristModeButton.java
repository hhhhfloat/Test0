package LoginPage.LoginButtons;

import LoginPage.Account;
import LoginPage.AccountScene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TouristModeButton {
    public static Button touristMode(Stage stage, Account account) {
        Button tourist = new Button("Tourist Mode");
        tourist.setPrefSize(400, 50);
        tourist.setOnAction(event -> stage.setScene(AccountScene.getAccountScene(stage, account, true)));
        return tourist;
    }
}
