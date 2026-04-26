package LoginPage;

import LoginPage.Buttons.*;
import Page.rsc.BgImage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AccountScene {
    public static Scene getAccountScene(Stage stage, Account account,Boolean tourist) {
        VBox vBox = new VBox(), list = new VBox();
        vBox.setBackground(BgImage.getBgImage());
        Label accountName = new Label();
        if(tourist) {
            accountName.setText("Tourist Mode");
            list.getChildren().addAll(accountName, LoginButton.login(stage, account));
        } else {
            accountName.setText("Welcome:" + account.getAccountName());
            list.getChildren().addAll(accountName, LogoutButton.logoutButton(stage));
        }
        list.getChildren().addAll(StartButton.startButton(stage), LeaderboardButton.leaderboard(account), SettingsButton.settings(), ExitButton.exit());
        vBox.getChildren().addAll(list);
        list.setAlignment(Pos.CENTER);
        return new Scene(vBox, BgImage.getImage().getWidth(), BgImage.getImage().getHeight());
    }
}
