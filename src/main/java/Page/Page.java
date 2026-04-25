package Page;

import LoginPage.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Page extends Application {

    public void start(Stage stage) {
        stage.setScene(LoginScene.getLoginScene(stage));
        stage.show();
    }
}
