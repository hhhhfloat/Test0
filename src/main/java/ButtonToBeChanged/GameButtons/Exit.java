package ButtonToBeChanged.GameButtons;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class Exit {
    public static Button exit() {
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(400, 50);
        exitButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("All the unsaved data will be lost!");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.exit();
                }
            });
        });
        return exitButton;
    }
}
