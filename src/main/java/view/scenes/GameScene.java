package view.scenes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import view.game_nodes.BoardInterface;

public class GameScene extends Scene{
    private BoardInterface board;
    private Label scoreLabel;
    private Label timeLabel;

    public GameScene(BoardInterface board) {
        super(new BorderPane(createRoot(board)),800,800);
        this.board = board;
    }

    private static BorderPane createRoot(BoardInterface board) {
        BorderPane root = new BorderPane((Node)board);
        return root;
    }
}
