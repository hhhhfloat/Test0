package modifying.shared.app;

import javafx.application.Application;
import javafx.stage.Stage;
import modifying.shared.controller.MainController;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        controller.start(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}