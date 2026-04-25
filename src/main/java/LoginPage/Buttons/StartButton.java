package LoginPage.Buttons;

import javafx.scene.control.*;

public class StartButton {
    public static Button startButton() {
        Button startButton = new Button("Start");
        startButton.setPrefSize(400, 50);
        startButton.setOnAction(event -> {

        });
        return startButton;
    }
}
