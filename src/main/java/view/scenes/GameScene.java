package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.game_nodes.CellNode;

public class GameScene extends Scene{

    public GameScene() {

    }

    private static StackPane createRoot(GameCtrl controller) {
        StackPane root = new StackPane();

        // 创建棋盘
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        // 这里需要从 Model 获取初始数据
        // 暂时用假数据，后续由 controller 初始化
        int[][] testData = getTestData();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                CellNode cell = new CellNode(row, col, testData[row][col]);
                final int r = row, c = col;
                cell.setOnMouseClicked(e -> controller.handleCellClick(r, c));
                grid.add(cell, col, row);
            }
        }

        root.getChildren().add(grid);
        return root;
    }
}
