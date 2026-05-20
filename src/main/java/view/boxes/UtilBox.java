package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class UtilBox extends VBox{
    ToggleButton bombButton;
    public UtilBox(GameCtrl gameCtrl){
        super(40);
        Button freezeButton = new Button("Freeze :x"), hintButton = new Button("Hint: x");
        bombButton = new ToggleButton("Bomb: x");
        bombButton.setOnMouseClicked(event -> gameCtrl.handleBombMode());
        freezeButton.setOnMouseClicked(event -> {
            try {
                gameCtrl.handleFreeze();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        hintButton.setOnMouseClicked(event -> gameCtrl.handleHint());

        getChildren().addAll(bombButton, freezeButton, hintButton);
    }
    public void bombLightOff(){
        bombButton.setSelected(false);
    }
}
