package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoadBox extends VBox{
    public LoadBox(GameCtrl gameCtrl) {
        super(20);
        Button save1 = new Button("Load 1"), save2 = new Button("Load 2"), save3 = new Button("Load 3"), delete1 = new Button(), delete2 = new Button(), delete3 = new Button(), back = new Button("Back");
        save1.setOnAction(event -> gameCtrl.handleLoad1());
        save2.setOnAction(event -> gameCtrl.handleLoad2());
        save3.setOnAction(event -> gameCtrl.handleLoad3());
        save1.getStyleClass().add("load");
        save2.getStyleClass().add("load");
        save3.getStyleClass().add("load");
        delete1.setOnMouseClicked(event -> gameCtrl.handleDelete1());
        delete2.setOnMouseClicked(event -> gameCtrl.handleDelete2());
        delete3.setOnMouseClicked(event -> gameCtrl.handleDelete3());
        delete1.getStyleClass().add("cross");
        delete2.getStyleClass().add("cross");
        delete3.getStyleClass().add("cross");
        back.setOnAction(event -> gameCtrl.handleBack());
        HBox load1 = new HBox(20, save1, delete1), load2 = new HBox(20, save2, delete2), load3 = new HBox(20, save3, delete3);;
        VBox vBox = new VBox(20, load1, load2, load3, back);
        getChildren().addAll(vBox);
    }
}
