package view.scene.Boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class LevelBox {
    public static VBox levelBtnBox(){
        Button easyBtn = ButtonUtils.createButton("Easy");
        easyBtn.setOnAction(event -> GameCtrl.handleEasy());
        Button difficultBtn = ButtonUtils.createButton("Difficult");
        difficultBtn.setOnAction(event -> GameCtrl.handleDifficult());
        VBox vBox = new VBox(10, easyBtn, difficultBtn);
        return vBox;
    }
}
