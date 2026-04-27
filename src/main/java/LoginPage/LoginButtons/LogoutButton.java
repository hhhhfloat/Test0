package LoginPage.LoginButtons;

import LoginPage.LoginScene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LogoutButton {
    public static Button logoutButton(final Stage stage) {
        Button LogoutButton = new Button("Logout");
        LogoutButton.setPrefSize(400, 50);
        LogoutButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.showAndWait();
            stage.setScene(LoginScene.getLoginScene(stage));
        });
        return LogoutButton;
    }
}
