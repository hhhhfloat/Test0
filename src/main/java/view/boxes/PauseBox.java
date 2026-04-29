package view.boxes;

import controller.GameCtrl;
import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PauseBox extends VBox{
    public PauseBox(GameCtrl gameCtrl) {
        super(10);
        Button restartBtn = new Button("Save");
        restartBtn.setOnAction(event -> gameCtrl.handleRestart());
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(event -> gameCtrl.handleSave());
        Button continueBtn = new Button("Continue");
        continueBtn.setOnAction(event ->{

        });
        Button exitToMenuBtn = new Button("Exit To Menu");
        exitToMenuBtn.setOnAction(event -> gameCtrl.handleExitToMenu());
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(event -> gameCtrl.handleExit());
        getChildren().addAll(restartBtn, saveBtn, exitToMenuBtn, exitBtn);
    }
}
