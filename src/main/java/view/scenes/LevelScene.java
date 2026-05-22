package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private int[] maxUnlocked = {1,1,1};
    private boolean isPlayInfo = false;

    public LevelScene(GameCtrl gameCtrl) {
        super(root, 800, 800);
        levelBox = new LevelBox(gameCtrl);
        this.gameCtrl = gameCtrl;
        root.getChildren().add(levelBox);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "levelSceneStyle.css");
        getStylesheets().add(cssPath.toUri().toString());
    }

    public void playInfo(int loadNumber) {
        congrats = "     Congratulations!\nLevel "+maxUnlocked[loadNumber]+" Unlocked!";
        if(isPlayInfo) {
            InformationUtil.playInformation(root, congrats);
            isPlayInfo = false;
        }
    }
    public void unlock(int currentLevel, int loadNumber){
        int levelIndex = currentLevel + 1;
        Button level = levelBox.getLevel(levelIndex++);
        level.setText("Level"+levelIndex);
        final int unlockIndex = levelIndex;
        level.setOnAction(e->gameCtrl.handleLevel(unlockIndex));
        maxUnlocked[loadNumber] = levelIndex;
        isPlayInfo = true;
    }
}
