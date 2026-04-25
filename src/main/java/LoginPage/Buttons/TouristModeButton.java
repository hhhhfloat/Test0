package LoginPage.Buttons;

import javafx.scene.control.Button;

public class TouristModeButton {
    public static Button touristMode() {
        Button tourist = new Button("Tourist Mode");
        tourist.setPrefSize(400, 50);
        tourist.setOnAction(event -> {

        });
        return tourist;
    }
}
