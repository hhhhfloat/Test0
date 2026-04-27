package GamePage.GameButtons;

import javafx.scene.control.Button;

public class Save {
    public static Button saveButton() {
        Button save = new Button("Save");
        save.setPrefSize(100,50);
        save.setOnAction(event -> {

        });
        return save;
    }
}
