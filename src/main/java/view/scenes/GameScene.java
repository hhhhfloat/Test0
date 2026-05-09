package view.scenes;

import controller.GameCtrl;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.entity.LinkyMap;
import view.game_nodes.CellNode;

public class GameScene extends Scene{
    private GameCtrl gameCtrl;
    private LinkyMap linkyMap;
    private Label scoreLabel;
    private Label timeLabel;
    private Label remainLabel;
    private GridPane gridPane;
    private CellNode[][] cellNodes;

    public GameScene(GameCtrl gameCtrl, LinkyMap linkyMap) {
        super(new BorderPane(createRoot(gameCtrl, linkyMap)),800,600);
        this.linkyMap = linkyMap;
        this.gameCtrl = gameCtrl;
    }

    private static StackPane createRoot(GameCtrl gameCtrl, LinkyMap linkyMap) {
        Group grid = new Group();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                CellNode cellNode = new CellNode(i, j, 50, linkyMap.getMap()[i][j], gameCtrl);
                grid.getChildren().addAll(cellNode);
            }
        }
        StackPane root = new StackPane(grid);
        return root;
    }

    public void updateTime(int seconds)
    {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d",minutes,secs));
    }
}
