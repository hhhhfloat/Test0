package LoginPage;

import javafx.application.Application;
import javafx.stage.Stage;

public class Login extends Application {

    public void start(Stage stage) {
        stage.setScene(LoginScene.loginScene(stage));
        stage.show();
    }
}
