package before.view.boxes;

import before.controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class UtilBox extends VBox{
    private final ToggleButton bombButton;
    private final Button freezeButton, hintButton;
    private boolean isFreeze;
    public UtilBox(GameCtrl gameCtrl){
        super(40);
        freezeButton = new Button("⌛ ×" + gameCtrl.getFreezeCount());
        hintButton = new Button("❓ ×"+gameCtrl.getHintCount());
        bombButton = new ToggleButton("💥 ×"+gameCtrl.getBombCount());
        bombButton.setOnMouseClicked(event -> gameCtrl.handleBombMode());

        if(!isFreeze)
            freezeButton.setOnMouseClicked(event -> gameCtrl.handleFreeze());
        hintButton.setOnMouseClicked(event -> gameCtrl.handleHint());

        getChildren().addAll(bombButton, freezeButton, hintButton);
    }
    public void bombLightOff(){
        bombButton.setSelected(false);
    }
    public void setBombCount(int count){
        bombButton.setText("💥 ×"+count);
    }
    public void setFreezeCount(int count){freezeButton.setText("⌛ ×"+count);}
    public void setHintCount(int count){hintButton.setText("❓ ×"+count);}

}
