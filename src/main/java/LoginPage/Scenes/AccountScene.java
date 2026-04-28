package LoginPage.Scenes;

import LoginPage.Account;
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
            list.getChildren().addAll(accountName, Login.login(stage, account));
        } else {
            accountName.setText("Welcome:" + account.getAccountName());
            list.getChildren().addAll(accountName, Logout.logoutButton(stage));
        }
        list.getChildren().addAll(Start.startButton(stage), Leaderboard.leaderboard(account), Settings.settings(), Exit.exit());
        StackPane stackPane = new StackPane(list);
        list.setAlignment(Pos.CENTER);
        stackPane.setBackground(BgImage.getBgImage());
        StackPane.setMargin(list, new Insets(100, 0, 0, 0));

        return new Scene(stackPane, BgImage.getImage().getWidth(), BgImage.getImage().getHeight());
    }
}
