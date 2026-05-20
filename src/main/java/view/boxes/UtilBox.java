package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class UtilBox extends VBox{
    public UtilBox(GameCtrl gameCtrl){
        super(40);
        Button bombButton = new Button("Bomb: x"), freezeButton = new Button("Freeze :x"), hintButton = new Button("Hint: x");

        bombButton.setOnMouseClicked(event -> gameCtrl.handleBombMode());
        freezeButton.setOnMouseClicked(event -> {gameCtrl.handleFreeze();});
        hintButton.setOnMouseClicked(event -> gameCtrl.handleHint());

        getChildren().addAll(bombButton, freezeButton, hintButton);
    }
}
