package LoginPage;

import LoginPage.Buttons.*;
import Page.rsc.BgImage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AccountScene {
    public static Scene getAccountScene(Stage stage, Account account, Boolean tourist) {
        VBox list = new VBox(20);
        Label accountName = new Label();
        if (tourist) {
            accountName.setText("Tourist Mode");
            list.getChildren().addAll(accountName, LoginButton.login(stage, account));
        } else {
            accountName.setText("Welcome:" + account.getAccountName());
            list.getChildren().addAll(accountName, LogoutButton.logoutButton(stage));
        }
        list.getChildren().addAll(StartButton.startButton(stage), LeaderboardButton.leaderboard(account), SettingsButton.settings(), ExitButton.exit());
        StackPane stackPane = new StackPane(list);
        list.setAlignment(Pos.CENTER);
        stackPane.setBackground(BgImage.getBgImage());
        StackPane.setMargin(list, new Insets(100, 0, 0, 0));

        return new Scene(stackPane, BgImage.getImage().getWidth(), BgImage.getImage().getHeight());
    }
}
