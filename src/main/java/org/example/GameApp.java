package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class GameApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Click on me");
        btn.setOnAction(e -> btn.setText("Literally clicked!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("JavaFX  test  window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
