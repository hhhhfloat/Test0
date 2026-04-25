package LoginPage;

import LoginPage.Buttons.*;
import Page.rsc.BgImage;
import Page.rsc.BgMusic;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class AccountScene {
    public static Scene getAccountScene(Stage stage) {

        BgMusic.play();
        VBox vBox = new VBox(), list = new VBox();
        vBox.setBackground(BgImage.getBgImage());

        list.getChildren().addAll(StartButton.startButton(stage), LogoutButton.logoutButton(stage), LeaderboardButton.leaderboard(), SettingsButton.settings(), ExitButton.exit());
        vBox.getChildren().addAll(list);
        list.setAlignment(Pos.CENTER);

        return new Scene(vBox, BgImage.getImage().getWidth(), BgImage.getImage().getHeight());
    }
}
