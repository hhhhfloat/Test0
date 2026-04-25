package LoginPage;

import LoginPage.Buttons.ExitButton;
import LoginPage.Buttons.LeaderboardButton;
import LoginPage.Buttons.LoginButton;
import LoginPage.Buttons.SettingsButton;
import LoginPage.rsc.BgImage;
import LoginPage.rsc.BgMusic;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScene {
    public static Scene loginScene(Stage stage) {
        BgMusic.play();
        LoginButton buttons = new LoginButton(Accounts.read());
        VBox vBox = new VBox(), list =new VBox();
        vBox.setBackground(BgImage.bgimage());

        list.getChildren().addAll(buttons.login(stage), LeaderboardButton.leaderboard(), SettingsButton.settings(), ExitButton.exit());
        vBox.getChildren().addAll(list);
        list.setAlignment(Pos.CENTER);

        return new Scene(vBox,1427,994);
    }
}
