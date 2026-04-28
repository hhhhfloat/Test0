package view.boxes;

import controller.GameCtrl;
import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.common.ButtonUtils;

public class PauseBox extends VBox{
    public PauseBox(GameCtrl gameCtrl) {
        super(10);
        Button restartBtn = ButtonUtils.createButton("Save");
        restartBtn.setOnAction(event -> gameCtrl.handleRestart());
        Button saveBtn = ButtonUtils.createButton("Save");
        saveBtn.setOnAction(event -> gameCtrl.handleSave());
        Button continueBtn = ButtonUtils.createButton("Continue");
        continueBtn.setOnAction(event ->{

        });
        Button exitToMenuBtn = ButtonUtils.createButton("Exit");
        exitToMenuBtn.setOnAction(event -> gameCtrl.handleExitToMenu());
        Button exitBtn = ButtonUtils.createButton("Exit");
        exitBtn.setOnAction(event -> gameCtrl.handleExit());
        getChildren().addAll(restartBtn, saveBtn, exitToMenuBtn, exitBtn);
    }
}
