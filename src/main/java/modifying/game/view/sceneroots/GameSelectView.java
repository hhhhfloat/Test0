package modifying.game.view.sceneroots;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.game.controller.GameCtrl;

import java.nio.file.Paths;

public class GameSelectView extends StackPane {

    private final VBox gameSelectBox;
    private final GameCtrl gameCtrl;

    public GameSelectView(GameCtrl gameCtrl){
        this.gameCtrl = gameCtrl;

        gameSelectBox = new VBox(15);
        gameSelectBox.setAlignment(Pos.CENTER);
        initGameSelectBox();

        getChildren().add(gameSelectBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "GameSceneStyle", "gameSelectScene.css").toUri().toString());

    }

    private void initGameSelectBox(){

    }

}
