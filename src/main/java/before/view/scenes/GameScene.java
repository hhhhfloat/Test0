package before.view.scenes;

import before.controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import before.view.InformationUtil;
import before.view.boxes.InfoBox;
import before.view.boxes.UtilBox;
import before.view.game_nodes.Board;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GameScene extends Scene {
    private static StackPane root;
    private static UtilBox utilBox;
    private static InfoBox infoBox;

    static Board gameBoard;

    public GameScene(GameCtrl gameCtrl) {
        super(new BorderPane(createRoot(gameCtrl)), 800, 800);
        Path cssPath = Paths.get("src", "main", "resources", "css", "SceneStyle", "gameSceneStyle.css");
        String cssUri = cssPath.toUri().toString();
        getStylesheets().add(cssUri);
    }

    private static StackPane createRoot(GameCtrl gameCtrl) {
        Pane underPane = new Pane();
        root = new StackPane();

        Button pauseButton = new Button();
        pauseButton.setOnAction(event -> gameCtrl.handlePause());
        pauseButton.getStyleClass().add("pausebutton");
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(15);

        gameBoard = (Board) gameCtrl.getBoard();
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

    public static void playInfo(String s){
        InformationUtil.playInformation(root, s);
    }

    public static void bombLightOff(){
        utilBox.bombLightOff();
    }
    public static void updateBombBtn(int count){
        utilBox.setBombCount(count);
    }
    public static void updateHintBtn(int count){ utilBox.setHintCount(count);}
    public static void updateFreezeBtn(int count){utilBox.setFreezeCount(count);}
    public static void freezeTime(boolean isFreeze){
        infoBox.freezeTime(isFreeze);
    }
}
