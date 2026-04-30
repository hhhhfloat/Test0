package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LoadBox extends VBox{
    public LoadBox(GameCtrl gameCtrl) {
        super(20);
        Button save1 = new Button("Load 1");
        save1.setOnAction(event -> gameCtrl.handleLoad1());
        Button save2 = new Button("Load 2");
        save2.setOnAction(event -> gameCtrl.handleLoad2());
        Button save3 = new Button("Load 2");
        save3.setOnAction(event -> gameCtrl.handleLoad3());
        Button back = new Button("Back");
        back.setOnAction(event -> gameCtrl.handleBack());
        getChildren().addAll(save1, save2, save3, back);
    }
}
