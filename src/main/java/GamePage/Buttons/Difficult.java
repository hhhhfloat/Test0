package GamePage.Buttons;

import javafx.scene.control.Button;

public class Difficult {
    public static Button difficult() {
        Button difficultMode  = new Button("DIFFICULT");
        difficultMode.setPrefSize(50, 400);
        difficultMode.setOnAction(event -> {

        });
        return difficultMode;
    }
}
