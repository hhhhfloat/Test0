package modifying.game.view.implement;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameCtrl;
import modifying.game.controller.GameSceneCtrl;
import modifying.game.view.GameWindow;
import modifying.game.view.IGameScene;
import modifying.shared.resources.ResourceManager;
import modifying.shared.view.UIUtils;

import java.util.Objects;

import static java.lang.Math.*;

public class LobbyView extends StackPane implements IGameScene, CameraController.BoundsProvider {

    private static final double WORLD_W = GameSceneCtrl.getWorldWidth();
    private static final double WORLD_H = GameSceneCtrl.getWorldHeight();

    private final GameCtrl gameCtrl;
    private final CameraController camera;

    private Group mapGroup;
    private Group buttonGroup;

    private final Scale mapScale = new Scale(1, 1);
    private static final double MAX_SCALE = 2.0;
    private static final double MIN_SCALE = 0.2;
    private final StackPane contentPane;
    public LobbyView(GameCtrl gameCtrl, StackPane contentPane) {
        this.gameCtrl = gameCtrl;
        this.contentPane = contentPane;
        contentPane.setAlignment(Pos.TOP_LEFT);

        getStylesheets().add(getClass().getResource("/css/GameSceneStyle/gameSceneStyle.css").toExternalForm());
        initMapGroup();

        // 设置摄像机边界提供者为本对象
        camera = new CameraController(this, contentPane.getWidth(), contentPane.getHeight());
        initCamera();

        setupScrollZoom();
    }

    private void initMapGroup(){
        Image mapImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/Sprites/sprites/grid_background.png")
                )
        );
        ImageView mapBackground = new ImageView(mapImage);
        mapBackground.setFitWidth(WORLD_W);
        mapBackground.setFitHeight(WORLD_H);
        mapBackground.setPreserveRatio(false);

        buttonGroup = new Group();
        mapGroup = new Group(mapBackground, buttonGroup);
        mapGroup.getTransforms().add(mapScale);
        getChildren().add(mapGroup);
        gameCtrl.getGameSceneCtrl().setMapGroup(mapGroup);

        addGameEntry("snake", 500, 600);
        addGameEntry("link link", 1200, 800);
    }

    private void initCamera(){
        contentPane.widthProperty().addListener(
                (obs, old, newVal) ->{
                    if(newVal.doubleValue() > 0){
                        camera.updateViewport(contentPane.getWidth(), contentPane.getHeight());
                    }
                }
        );
        contentPane.heightProperty().addListener(
                (obs, old, newVal)->{
                    if(newVal.doubleValue() > 0){
                        camera.updateViewport(contentPane.getWidth(), contentPane.getHeight());
                    }
                }
        );
        camera.setBoundsProvider(this);
        camera.attachTo(mapGroup);
        camera.resetToCenter();
    }

    @Override
    public Bounds getVisualBounds() {
        return mapGroup.getBoundsInParent();
    }



    private void setupScrollZoom() {
        this.setOnScroll(e -> {
            if (e.isControlDown() || e.isShiftDown() || e.isAltDown()) return;
            double delta = e.getDeltaY();
            double oldScale = camera.getCurrentScale();
            double newScale = calcNewScale(delta);
            if (Math.abs(newScale - oldScale) < 0.001) return;

            double sceneX = e.getSceneX();
            double sceneY = e.getSceneY();

            // 获取世界坐标（基于 contentContainer）
            Point2D worldPoint = mapGroup.sceneToLocal(sceneX, sceneY);
            double worldX = worldPoint.getX();
            double worldY = worldPoint.getY();

            double oldPivotX = mapScale.getPivotX();
            double oldPivotY = mapScale.getPivotY();

            double newPivotX, newPivotY;

            if (Math.abs(1.0 - newScale) < 1e-12) {
                newPivotX = worldX;
                newPivotY = worldY;
            } else {
                // 推导出的精确锁定公式：
                // (W * newS) + P_new * (1 - newS) = (W * oldS) + P_old * (1 - oldS)
                newPivotX = (worldX * (oldScale - newScale) + oldPivotX * (1 - oldScale)) / (1 - newScale);
                newPivotY = (worldY * (oldScale - newScale) + oldPivotY * (1 - oldScale)) / (1 - newScale);
            }
            // 应用缩放
            mapScale.setPivotX(newPivotX);
            mapScale.setPivotY(newPivotY);
            mapScale.setX(newScale);
            mapScale.setY(newScale);

            // 更新摄像机边界并触发平滑修正（内部会调用 getVisualBounds）
            camera.setScale(newScale);
        });
    }

    private double calcNewScale(double delta) {
        double factor = (delta > 0) ? 1.1 : 1 / 1.1;
        double temp = camera.getCurrentScale() * factor;
        double minScale = max(camera.getViewportWidth() / WORLD_W, camera.getViewportHeight() / WORLD_H) + 0.01;
        return clamp(temp, minScale, MAX_SCALE);
    }



    private void addGameEntry(String text, double x, double y) {
        ImageView buttonFrame = ResourceManager.getInstance()
                .getGameAtlas().createImageView("try.png");
        buttonFrame.setFitWidth(120);
        buttonFrame.setFitHeight(50);
        buttonFrame.setPreserveRatio(false);

        Button btn = new Button(text);
        btn.setGraphic(buttonFrame);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.setAlignment(Pos.CENTER);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setLayoutX(x - 60);
        btn.setLayoutY(y - 25);
        btn.getStyleClass().add("lobby-btn");   // ✅ 添加样式类
        btn.setOnAction(e -> {
            String gameId = mapTextToGameId(text);
            // 让窗口出现在按钮右下侧（避免完全遮挡按钮）
            gameCtrl.openGameWindow(gameId, x + 80, y - 20);
        });
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
        this.setMouseTransparent(true);
    }

    @Override
    public void onResume() {
        this.setMouseTransparent(false);
    }

    @Override
    public void update(long now) {
    }

    private void refreshUserData() {
    }
}