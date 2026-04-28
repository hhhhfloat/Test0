package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class LoadBox extends VBox{
    public LoadBox(GameCtrl gameCtrl) {
        super(10);
        Button save1 = ButtonUtils.createButton("Load 1");
        save1.setOnAction(event -> {

        });
        Button save2 = ButtonUtils.createButton("Load 2");
        save2.setOnAction(event -> {

        });
        Button save3 = ButtonUtils.createButton("Load 2");
        save3.setOnAction(event -> {

        });
        getChildren().addAll(save1, save2, save3);
    }
}
