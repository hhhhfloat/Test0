package view.game;

import controller.GameContr;
import javafx.scene.control.Button;
import view.common.ButtonUtils;

import javax.swing.plaf.basic.BasicTableUI;

public class GameScene {
    public void gamescene()
    {
        Button pause_btn = ButtonUtils.createTextButton("pause");
        pause_btn.setOnAction(e -> GameContr.handlePause());
    }
}
