package LoginPage;

import LoginPage.rsc.BgImage;
import LoginPage.rsc.BgMusic;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Login extends Application {

    public void start(Stage stage) {
        //BgMusic.play();
        LoginButtons buttons = new LoginButtons(Accounts.read());
        VBox vBox = new VBox(), list =new VBox();
        vBox.setBackground(BgImage.bgimage());

        list.getChildren().addAll(buttons.login(), buttons.exit());
        vBox.getChildren().addAll(list);
        list.setAlignment(Pos.CENTER);

        stage.setTitle("Link Game");
        stage.setScene(new Scene(vBox,1427,994));
        stage.show();
    }
}
