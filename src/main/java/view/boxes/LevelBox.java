package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LevelBox extends VBox{
    public LevelBox(GameCtrl gameCtrl){
        super(40);
        Button level1 = new Button("Level 1"),  level2 = new Button("Level 2"),  level3 = new Button("Level 3"), level4 = new Button("Level 4"),  level5 = new Button("Level 5");
        level1.setOnAction(event -> gameCtrl.handleLevel1());
        level2.setOnAction(event -> gameCtrl.handleLevel2());
        level3.setOnAction(event -> gameCtrl.handleLevel3());
        level4.setOnAction(event -> gameCtrl.handleLevel4());
        level5.setOnAction(event -> gameCtrl.handleLevel5());
        getChildren().addAll(level1, level2, level3, level4, level5);
    }
}
