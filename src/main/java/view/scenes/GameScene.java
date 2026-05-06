package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.entity.Crd;
import view.game_nodes.CellNode;

public class GameScene extends Scene{

    private static final int ROWS = 12;
    private static final int COLS = 12;
    private GameCtrl ctrl;
    private Label scoreLabel;
    private Label timeLabel;
    private Label remainLabel;
    private GridPane gridPane;
    private CellNode[][] cellNodes;

    public GameScene(GameCtrl ctrl) {
        super(new BorderPane(),800,600);
        this.ctrl = ctrl;
    }

    private static StackPane createRoot(GameCtrl controller) {
        StackPane root = new StackPane();

        // 创建棋盘
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        // 这里需要从 Model 获取初始数据
        // 暂时用假数据，后续由 controller 初始化
        int[][] testData = new int[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                CellNode cell = new CellNode(row, col, testData[row][col],1);
                final int r = row, c = col;
                cell.setOnMouseClicked(e -> controller.handleCellClick(new Crd(r, c)));
                grid.add(cell, col, row);
            }
        }

        root.getChildren().add(grid);
        return root;
    }

    public void updateTime(int seconds)
    {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d",minutes,secs));
    }
}
