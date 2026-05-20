package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.InformationUtil;
import view.boxes.LevelBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LevelScene extends Scene{
    private static LevelBox levelBox;
    private final GameCtrl gameCtrl;
    private String congrats;
    private static final StackPane root = new StackPane();
    private int unlock;
    private boolean isPlayInfo = false;

    public LevelScene(GameCtrl gameCtrl) {
        super(root, 800, 800);
        levelBox = new LevelBox(gameCtrl);
        this.gameCtrl = gameCtrl;
        root.getChildren().add(levelBox);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "levelSceneStyle.css");
        getStylesheets().add(cssPath.toUri().toString());
    }

    public void playInfo() {
        switch(unlock) {
            case 2:
                congrats = "Congrats! Level2 - Unlocked!";
                break;
            case 3:
                congrats = "Congrats! Level3 - Unlocked!";
                break;
            case 4:
                congrats = "Congrats! Level4 - Unlocked!";
                break;
            case 5:
                congrats = "Congrats! Level5 - Unlocked!";
                break;
        }

        if(isPlayInfo) {
            InformationUtil.playInformation(root, congrats);
            isPlayInfo = false;
        }
    }

    public void unlock2(){
        levelBox.getLevel2().setText("Level2");
        levelBox.getLevel2().setOnAction(event -> gameCtrl.handleLevel2());
        unlock = 2;
        isPlayInfo = true;
    }

    public void unlock3(){
        levelBox.getLevel3().setText("Level3");
        levelBox.getLevel3().setOnAction(event -> gameCtrl.handleLevel3());
        unlock = 3;
        isPlayInfo = true;
    }

    public void unlock4(){
        levelBox.getLevel4().setText("Level4");
        levelBox.getLevel4().setOnAction(event -> gameCtrl.handleLevel4());
        unlock = 4;
        isPlayInfo = true;
    }

    public void unlock5(){
        levelBox.getLevel5().setText("Level5");
        levelBox.getLevel5().setOnAction(event -> gameCtrl.handleLevel5());
        unlock = 5;
        isPlayInfo = true;
    }
}
