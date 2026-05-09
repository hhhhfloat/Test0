package view.scenes;

import controller.GameCtrl;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.entity.LinkyMap;
import view.game_nodes.Board;

public class GameScene extends Scene{
    private final GameCtrl gameCtrl;
    private LinkyMap linkyMap;
    private static Board board;
    private Label scoreLabel;
    private Label timeLabel;
    private Label remainLabel;

    public GameScene(GameCtrl gameCtrl, LinkyMap linkyMap) {
        super(new BorderPane(createRoot(gameCtrl, linkyMap)),800,600);
        this.linkyMap = linkyMap;
        this.gameCtrl = gameCtrl;
    }

    public Board getBoard() {
        return board;
    }

    private static StackPane createRoot(GameCtrl gameCtrl, LinkyMap linkyMap) {
        board = new Board(12, 12, 50, linkyMap, gameCtrl);
        StackPane root = new StackPane(board);
        return root;
    }

    public void updateTime(int seconds)
    {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d",minutes,secs));
    }
}
