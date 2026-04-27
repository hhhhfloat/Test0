package ButtonToBeChanged.GameButtons;

import ButtonToBeChanged.LoginButtons.SettingsButton;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Pause {
    public static  Button save() {
        Button saveButton = new Button("Save");
        saveButton.setPrefSize(400,50);
        saveButton.setOnAction(event -> {

        });
        return saveButton;
    }

    public static Button exitToMenu() {
        Button exit = new Button("Exit to Main Menu");
        exit.setPrefSize(400,50);
        exit.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        });
        return exit;
    }

    public static Button exitToDesktop() {
        Button  exit = new Button("Exit to Desktop");
        exit.setPrefSize(400,50);
        exit.setOnAction(event -> {

        });
        return exit;
    }

    public static Button cancel() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(400, 50);
        cancelButton.setOnAction(event -> {

        });
        return cancelButton;
    }

    public static Button continue_() {
        Button continueButton = new Button("Continue");
        continueButton.setPrefSize(400,50);
        continueButton.setOnAction(event -> {

        });
        return continueButton;
    }

    public static  Button exit() {
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(400,50);
        exitButton.setOnAction(event -> {

        });
        return exitButton;
    }

    public static Button getButton() {
        Button pauseButton = new Button("Pause");
        pauseButton.setPrefSize(400, 50);
        pauseButton.setOnAction(event -> {
            VBox list = new VBox();
            list.getChildren().addAll(save(), exit(), SettingsButton.settings(), cancel());
            Stage pauseStage = new Stage();
            pauseStage.setScene(new Scene(list));
            pauseStage.show();
        });
        return pauseButton;
    }
}