package modifying.game.view.implement;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.game.view.IGameScene;
import modifying.shared.view.UIUtils;

import java.util.Objects;

import static java.lang.Math.*;

public class GameSelectView extends StackPane implements IGameScene, CameraController.BoundsProvider {

    private static final double WORLD_W = GameSceneCtrl.getWorldWidth();
    private static final double WORLD_H = GameSceneCtrl.getWorldHeight();

    private final GameCtrl gameCtrl;
    private final CameraController camera;

    private final Group mapGroup;
    private final ImageView mapBackground;
    private final Group buttonGroup;

    private final Scale mapScale = new Scale(1, 1);
    private double currentScale = 1.0;
    private static final double MAX_SCALE = 2.0;
    private static final double MIN_SCALE = 0.2;

    public GameSelectView(GameCtrl gameCtrl, CameraController camera) {
        this.gameCtrl = gameCtrl;
        this.camera = camera;

        Image mapImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/Sprites/sprites/grid_background.png")
                )
        );
        mapBackground = new ImageView(mapImage);
        mapBackground.setFitWidth(WORLD_W);
        mapBackground.setFitHeight(WORLD_H);
        mapBackground.setPreserveRatio(false);

        buttonGroup = new Group();
        mapGroup = new Group(mapBackground, buttonGroup);
        mapGroup.getTransforms().add(mapScale);
        getChildren().add(mapGroup);

        addGameEntry("snake", 500, 600);
        addGameEntry("link link", 1200, 800);

        // 设置摄像机边界提供者为本对象
        camera.setBoundsProvider(this);
        camera.attachTo(mapGroup);
        camera.resetToCenter();

        setupScrollZoom();
    }

    @Override
    public Bounds getVisualBounds() {
        // 返回 mapGroup 在父容器（GameSelectView）中的真实视觉边界
        // 因为 GameSelectView 与 contentContainer 对齐，所以这个边界就是世界坐标系中的真实范围
        return mapGroup.getBoundsInParent();
    }



    private void setupScrollZoom() {
        this.setOnScroll(e -> {
            if (e.isControlDown() || e.isShiftDown() || e.isAltDown()) return;
            double delta = e.getDeltaY();
            double newScale = calcNewScale(delta);
            if (Math.abs(newScale - currentScale) < 0.001) return;

            // 获取世界坐标（基于 contentContainer）
            Node worldNode = camera.getTargetNode();
            Point2D worldPoint = worldNode.sceneToLocal(e.getSceneX(), e.getSceneY());
            double worldX = worldPoint.getX();
            double worldY = worldPoint.getY();

            // 应用缩放
            mapScale.setPivotX(worldX);
            mapScale.setPivotY(worldY);
            mapScale.setX(newScale);
            mapScale.setY(newScale);
            currentScale = newScale;

            // 更新摄像机边界并触发平滑修正（内部会调用 getVisualBounds）
            camera.setScale(newScale);
        });
    }

    private double calcNewScale(double delta) {
        double factor = (delta > 0) ? 1.1 : 1 / 1.1;
        double temp = currentScale * factor;
        double minScale = max(camera.getViewportWidth() / WORLD_W, camera.getViewportHeight() / WORLD_H) + 0.01;
        return clamp(temp, minScale, MAX_SCALE);
    }



    private void addGameEntry(String text, double x, double y) {
        Button btn = new Button(text);
        btn.setLayoutX(x - 60);
        btn.setLayoutY(y - 25);
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
        btn.setOnAction(e -> gameCtrl.launchGame(mapTextToGameId(text)));
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
    }

    @Override
    public void onResume() {
    }

    @Override
    public void update(long now) {
    }

    private void refreshUserData() {
    }
}