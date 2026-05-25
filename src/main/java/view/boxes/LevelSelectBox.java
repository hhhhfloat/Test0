package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.Arrays;
import java.util.List;

public class LevelSelectBox extends VBox{
    private final List<Button> level = Arrays.asList(new Button("Level 0"),
            new Button("Level 1 - Locked"),
            new Button("Level 2 - Locked"),
            new Button("Level 3 - Locked") ,
            new Button("Level 4 - Locked"));
    public LevelSelectBox(GameCtrl gameCtrl){
        super(40);
        getStyleClass().add("levelbox");
        level.getFirst().setOnAction(event -> gameCtrl.handleLevel(0));
        Button back = new Button("Back");
        back.setOnMouseClicked(e->gameCtrl.handleBackFromLevelSelect());
        getChildren().addAll(level);
        getChildren().addAll(back);
    }

    public Button getLevel(int index){
        return level.get(index);
    }

}
