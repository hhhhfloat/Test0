package LoginPage;

import LoginPage.Buttons.*;
import LoginPage.rsc.BgImage;
import LoginPage.rsc.BgMusic;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountScene {
    public static Scene accountScene(Stage stage) {

        BgMusic.play();
        VBox vBox = new VBox(), list =new VBox();
        vBox.setBackground(BgImage.bgimage());

        list.getChildren().addAll(StartButton.startButton(), LogoutButton.logoutButton(stage), LeaderboardButton.leaderboard(), SettingsButton.settings(), ExitButton.exit());
        vBox.getChildren().addAll(list);
        list.setAlignment(Pos.CENTER);
        Scene accountScene = new Scene(vBox,1427,994);

        return accountScene;
    }
}
