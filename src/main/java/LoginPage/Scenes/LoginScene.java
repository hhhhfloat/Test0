package LoginPage.Scenes;

import GamePage.Scenes.LevelScene;
import LoginPage.Account;
import LoginPage.Buttons.*;
import Page.rsc.BgImage;
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
        admin.setOnAction(event -> stage.setScene(LevelScene.getLevelScene(stage)));

        Account account = new Account();
        VBox list = new VBox(20);
        list.getChildren().addAll(Login.login(stage, account), TouristMode.touristMode(stage, account), Leaderboard.leaderboard(account), Settings.settings(), Exit.exit(), admin);
        list.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane(list);
        pane.setBackground(BgImage.getBgImage());
        StackPane.setMargin(list, new Insets(100, 0, 0, 0));

        return new Scene(pane, BgImage.getWidth(), BgImage.getHeight());
    }
}
