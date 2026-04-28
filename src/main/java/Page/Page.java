package Page;

import LoginPage.Scenes.LoginScene;
import Page.rsc.BgImage;
import Page.rsc.BgMusic;
import javafx.application.Application;
import javafx.stage.Stage;

public class Page extends Application {
    public void start(Stage stage) {
        final double ASPECT_RATIO = BgImage.getWidth() / BgImage.getHeight();
        BgMusic.play();
        stage.setScene(LoginScene.getLoginScene(stage));
        stage.show();
    }
}
