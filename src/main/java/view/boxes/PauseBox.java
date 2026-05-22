package view.boxes;

import controller.GameCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PauseBox extends VBox{
    public PauseBox(GameCtrl gameCtrl) {
        super(10);
        Button restartBtn = new Button("Restart"), saveBtn = new Button("Save"), continueBtn = new Button("Continue"), exitToMenuBtn = new Button("Exit To Level Selection"), exitBtn = new Button("Exit");
        restartBtn.setOnAction(event -> gameCtrl.handleRestart());
        saveBtn.setOnAction(event -> gameCtrl.handleSave(true));
        continueBtn.setOnAction(e->gameCtrl.handleContinue());
        exitToMenuBtn.setOnAction(event -> gameCtrl.handleExitToLevelSelect());
        exitBtn.setOnAction(event -> gameCtrl.handleExit());
        getChildren().addAll(restartBtn, saveBtn, continueBtn, exitToMenuBtn, exitBtn);
    }
}
