package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelBox extends VBox{
    private List<Button> level = Arrays.asList(new Button("Level 1"),
            new Button("Level 2 - Locked"),
            new Button("Level 3 - Locked"),
            new Button("Level 4 - Locked") ,
            new Button("Level 5 - Locked"));
    public LevelBox(GameCtrl gameCtrl){
        super(40);
        level.getFirst().setOnAction(event -> gameCtrl.handleLevel(1));
        Button back = new Button("Back");
        back.setOnMouseClicked(e->gameCtrl.showLoadScene());
        getChildren().addAll(level);
        getChildren().addAll(back);
    }

    public Button getLevel(int index){
        return level.get(index);
    }
}
