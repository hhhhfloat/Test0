package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LevelBox extends VBox{
    private final Button level2 = new Button("Level 2 - Locked"),  level3 = new Button("Level 3 - Locked"), level4 = new Button("Level 4 - Locked"),  level5 = new Button("Level 5 - Locked");
    public LevelBox(GameCtrl gameCtrl){
        super(40);
        Button level1 = new Button("Level 1");
        level1.setOnAction(event -> gameCtrl.handleLevel1());
        getChildren().addAll(level1, level2, level3, level4, level5);
    }

    public Button getLevel2() {
        return level2;
    }

    public Button getLevel3() {
        return level3;
    }

    public Button getLevel4() {
        return level4;
    }

    public Button getLevel5() {
        return level5;
    }
}
