package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class LevelBox extends VBox{
    public LevelBox(GameCtrl gameCtrl){
        super(10);
        Button easyBtn = ButtonUtils.createButton("Easy");
        easyBtn.setOnAction(event -> gameCtrl.handleEasy());
        Button difficultBtn = ButtonUtils.createButton("Difficult");
        difficultBtn.setOnAction(event -> gameCtrl.handleDifficult());
        getChildren().addAll(easyBtn, difficultBtn);
    }
}
