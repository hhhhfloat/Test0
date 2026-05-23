package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import view.InformationUtil;
import view.boxes.LevelSelectBox;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LevelSelectScene extends Scene{
    private static LevelSelectBox levelSelectBox;
    private GameCtrl gameCtrl;
    private String congrats;
    private static StackPane root;
    private int maxUnlocked = 0;

    public LevelSelectScene(GameCtrl gameCtrl) {
        super(new StackPane(createRoot(gameCtrl)), 800, 800);
        this.gameCtrl = gameCtrl;
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "levelSceneStyle.css");
        getStylesheets().add(cssPath.toUri().toString());
    }

    private static StackPane createRoot(GameCtrl gameCtrl){
        root = new StackPane();
        levelSelectBox = new LevelSelectBox(gameCtrl);
        root.getChildren().add(levelSelectBox);
        return root;
    }

    public int getMaxUnlocked() {
        return maxUnlocked;
    }

    public void playInfo() {
        congrats = "     Congratulations!\nLevel "+maxUnlocked+" Unlocked!";
        InformationUtil.playInformation(root, congrats);
    }
    public void unlock(int currentLevel){
        final int unlockIndex = currentLevel + 1;
        if(unlockIndex>5)return;
        Button level = levelSelectBox.getLevel(unlockIndex);
        level.setText("Level"+unlockIndex);
        level.setOnAction(e->gameCtrl.handleLevel(unlockIndex));
        maxUnlocked = unlockIndex;
    }

    public void setUnlockedLevel(int maxLevel){
        for (int i = 0; i < maxLevel; i++) {
            unlock(i);
        }
    }
}
