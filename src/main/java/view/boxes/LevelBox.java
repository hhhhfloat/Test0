package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LevelBox extends VBox{
    private final GameCtrl gameCtrl;
    private final Button level1 = new Button("Level 1"),  level2 = new Button("Level 2 - Locked"),  level3 = new Button("Level 3 - Locked"), level4 = new Button("Level 4 - Locked"),  level5 = new Button("Level 5 - Locked");
    public LevelBox(GameCtrl gameCtrl){
        super(40);
        this.gameCtrl =gameCtrl;

        level1.setOnAction(event -> gameCtrl.handleLevel1());

        level3.setOnAction(event -> gameCtrl.handleLevel3());
        level4.setOnAction(event -> gameCtrl.handleLevel4());
        level5.setOnAction(event -> gameCtrl.handleLevel5());
        getChildren().addAll(level1, level2, level3, level4, level5);
    }

    public void Unlock(Button button) {

    }

    public void Unlock2(){
        level2.setText("Level2 - Unlocked!");
        level2.setOnAction(event -> gameCtrl.handleLevel2());
    }

    public void Unlock3(){
        level3.setText("Level3 - Unlocked!");
        level3.setOnAction(event -> gameCtrl.handleLevel3());
    }

    public void Unlock4(){
        level4.setText("Level4 - Unlocked!");
        level4.setOnAction(event -> gameCtrl.handleLevel4());
    }

    public void Unlock5(){
        level5.setText("Level5 - Unlocked!");
        level5.setOnAction(event -> gameCtrl.handleLevel5());
    }
}
