package modifying.game.view.implement;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import modifying.game.controller.GameCtrl;
import modifying.game.view.GameWindow;

public class SnakeGameWindow extends GameWindow {

    private final GameCtrl gameCtrl;

    public SnakeGameWindow(GameCtrl gameCtrl, Group mapGroup) {
        super("SNAKE", mapGroup, gameCtrl.getGameSceneCtrl());
        this.gameCtrl = gameCtrl;

        // 在内容区域放一个占位标签（以后可以替换成真正的游戏画布）
        Label placeholder = new Label("🐍 蛇\n(即将上线)");
        placeholder.getStyleClass().add("game-placeholder");
        getContentArea().getChildren().add(placeholder);

        // 你可以在这里初始化游戏组件，或者等到 onEnter 时再初始化
        System.out.println("🐍 Snake window created at (" + getLayoutX() + ", " + getLayoutY() + ")");
    }



    @Override
    protected void onClosed() {
        System.out.println("🐍 Snake window closed, cleaning up resources...");
        // 将来如果你在窗口里启动了 AnimationTimer，在这里停止它
        // 如果有音效循环，在这里停止它
    }

}