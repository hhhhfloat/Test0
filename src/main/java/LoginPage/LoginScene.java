package LoginPage;

import GamePage.GameScene;
import LoginPage.Buttons.*;
import Page.rsc.BgImage;
import Page.rsc.BgMusic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScene {

    public static Scene getLoginScene(Stage stage) {
        //test
        Button admin = new Button("Admin");
        admin.setOnAction(event -> stage.setScene(GameScene.getGameScene()));

        Account account = new Account();
        BgMusic.play();
        VBox list = new VBox(20);
        list.getChildren().addAll(LoginButton.login(stage, account), TouristModeButton.touristMode(stage, account), LeaderboardButton.leaderboard(account), SettingsButton.settings(), ExitButton.exit(), admin);
        list.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane();
        pane.setBackground(BgImage.getBgImage());
        pane.getChildren().add(list);
        StackPane.setMargin(list, new Insets(100, 0, 0, 0));

        return new Scene(pane, BgImage.getWidth(), BgImage.getHeight());
    }
}
