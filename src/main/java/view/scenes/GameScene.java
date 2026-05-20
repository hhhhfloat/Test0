package view.scenes;

import controller.GameCtrl;
import dao.impl.FileGameSaveDao;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import model.entity.Crd;
import view.boxes.InfoBox;
import view.boxes.UtilBox;
import view.game_nodes.Board;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GameScene extends Scene {
    static StackPane root = new StackPane();
    static UtilBox utilBox;
    static InfoBox infoBox;

    public GameScene(GameCtrl gameCtrl) {
        super(new BorderPane(createRoot(gameCtrl)), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "gameSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static StackPane createRoot(GameCtrl gameCtrl) {
        Pane underPane = new Pane();

        FileGameSaveDao fileGameSaveDao = (FileGameSaveDao) gameCtrl.getGameSaveDao();

        Button pauseButton = new Button();
        pauseButton.setOnMouseClicked(event -> gameCtrl.handlePause());
        pauseButton.getStyleClass().add("pausebutton");
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(15);

        Board gameBoard = (Board) gameCtrl.getBoard();
        gameBoard.setLayoutX(134);
        gameBoard.setLayoutY(155);

        utilBox = new UtilBox(gameCtrl);
        utilBox.setLayoutX(702);
        utilBox.setLayoutY(230);

        infoBox = new InfoBox(gameCtrl);
        infoBox.setLayoutX(180);
        infoBox.setLayoutY(20);

        underPane.getChildren().addAll(pauseButton, gameBoard, infoBox, utilBox);

        root.getChildren().add(underPane);
        return root;
    }

    public static void bombLightOff(){
        utilBox.bombLightOff();
    }
}
