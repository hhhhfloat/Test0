package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LevelBox extends VBox{
    public LevelBox(GameCtrl gameCtrl){
        super(40);
        Button easyBtn = new Button("Easy");
        easyBtn.setOnAction(event -> gameCtrl.handleEasy());
        Button difficultBtn = new Button("Difficult");
        difficultBtn.setOnAction(event -> gameCtrl.handleDifficult());
        getChildren().addAll(easyBtn, difficultBtn);
    }
}
