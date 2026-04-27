package GamePage;

import GamePage.GameButtons.Difficult;
import GamePage.GameButtons.Easy;
import GamePage.GameButtons.Fun;
import Page.rsc.BgImage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelScene {
    public static Scene getLevelScene(Stage stage) {
        VBox list = new VBox(100, Easy.easy(stage), Difficult.difficult(stage), Fun.fun(stage));
        StackPane stackPane = new StackPane(list);
        list.setAlignment(Pos.CENTER);
        stackPane.setBackground(BgImage.getBgImage());
        Scene level = new Scene(stackPane, BgImage.getWidth(), BgImage.getHeight());
        return level;
    }
}
