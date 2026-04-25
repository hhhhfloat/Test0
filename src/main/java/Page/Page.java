package Page;

import LoginPage.LoginScene;
import Page.rsc.BgImage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Page extends Application {

    public void start(Stage stage) {
        final double ASPECT_RATIO = BgImage.getWidth()/BgImage.getHeight();

        stage.setScene(LoginScene.getLoginScene(stage));
        stage.show();
    }
}
