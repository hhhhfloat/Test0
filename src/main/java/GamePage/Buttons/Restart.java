package GamePage.Buttons;

import javafx.scene.control.Button;

public class Restart {
    public static Button restartButton() {
        Button restart = new Button("Restart");
        restart.setPrefSize(100,50);
        restart.setOnAction(event -> {

        });
        return restart;
    }
}
