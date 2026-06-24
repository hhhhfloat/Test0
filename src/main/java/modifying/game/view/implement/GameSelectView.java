package modifying.game.view.implement;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.game.view.IGameScene;
import modifying.shared.view.UIUtils;

import java.nio.file.Paths;
import java.util.Objects;

public class GameSelectView extends StackPane implements IGameScene {

    // 从 GameSceneCtrl 获取世界尺寸（消除魔法数字）
    private static final double WORLD_W = GameSceneCtrl.getWorldWidth();
    private static final double WORLD_H = GameSceneCtrl.getWorldHeight();

    private final GameCtrl gameCtrl;
    private final CameraController camera;

    // 地图组（用于未来滚轮缩放）
    private final Group mapGroup;
    // 背景图片视图
    private final ImageView mapBackground;
    // 按钮容器
    private final Group buttonGroup;

    public GameSelectView(GameCtrl gameCtrl, CameraController camera) {
        this.gameCtrl = gameCtrl;
        this.camera = camera;

        // 1. 加载背景图片（请将图片放在 src/main/resources/images/map_background.png）
        Image mapImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/Sprites/sprites/grid_background.png")
                )
        );
        mapBackground = new ImageView(mapImage);


        // 确保图片尺寸与常量一致（如果图片本身是 3600x2500，自动适配）
        mapBackground.setFitWidth(WORLD_W);
        mapBackground.setFitHeight(WORLD_H);
        // 保持原比例（但如果图片比例与常量不一致，需调整，这里假定一致）
        mapBackground.setPreserveRatio(false);


        // 2. 按钮组
        buttonGroup = new Group();

        // 3. 地图组
        mapGroup = new Group(mapBackground, buttonGroup);

        this.getChildren().add(mapGroup);

        // 4. 添加游戏入口（坐标用绝对数字，因为相对地图位置是固定的）
        addGameEntry("snake", 500, 600);
        addGameEntry("link link", 1200, 800);

        // 5. 绑定摄像机（事件源是 mapGroup，移动目标是 contentContainer）
        camera.attachTo(mapGroup);

        // 6. 初始居中
        camera.resetToCenter();
    }

    // ----- 辅助方法：添加游戏入口按钮 -----
    private void addGameEntry(String text, double x, double y) {
        Button btn = new Button(text);
        btn.setLayoutX(x - 60);  // 假设按钮宽 120，居中
        btn.setLayoutY(y - 25);  // 假设按钮高 50，居中
        btn.setPrefSize(120, 50);
        btn.setStyle(
                "-fx-font-family: 'Comic Sans MS';" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-color: #f0e6d3;" +
                        "-fx-border-color: #8b7a66;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;"
        );
        // 点击事件：通过 GameCtrl 启动对应游戏
        btn.setOnAction(e -> gameCtrl.launchGame(mapTextToGameId(text)));
        // 应用通用悬浮缩放效果
        UIUtils.addHoverScale(btn);
        buttonGroup.getChildren().add(btn);
    }

    private String mapTextToGameId(String text) {
        if (text.contains("snake")) return "SNAKE";
        if (text.contains("link link")) return "LINK_LINK";
        return "UNKNOWN";
    }



    @Override
    public Parent getView() {
        return this;
    }

    @Override
    public void onEnter() {
        refreshUserData();
    }

    @Override
    public void onExit() {
        this.getChildren().clear();
    }

    @Override
    public void onPause() {
        // 大厅被覆盖（比如进入游戏）时，不需要特别操作
        // 但如果有背景动画，可以在这里暂停
    }

    @Override
    public void onResume() {
        // 大厅重新显示时，刷新排行榜或用户信息
    }

    @Override
    public void update(long now) {
        // 大厅界面的 update 通常什么都不做，或者做很轻量的 UI 闪烁
        // 留空即可
    }

    private void refreshUserData(){

    }
}
