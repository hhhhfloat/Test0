package modifying.game.view;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import modifying.game.controller.GameSceneCtrl;

import java.util.Objects;

public abstract class GameWindow extends StackPane {

    protected final Group mapGroup;
    protected final StackPane contentArea;

    private static final double WIDTH = 500;
    private static final double HEIGHT = 720;

    // 拖动状态
    private double dragStartSceneX, dragStartSceneY;
    private double layoutStartX, layoutStartY;
    protected final GameSceneCtrl gameSceneCtrl;
    private String windowId;

    public String getWindowId() {
        return windowId;
    }

    public GameWindow(String title, Group mapGroup, GameSceneCtrl gameSceneCtrl) {
        windowId = title;
        this.mapGroup = mapGroup;
        this.gameSceneCtrl = gameSceneCtrl;

        // ---------- 窗口大小和样式 ----------
        Image mapImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/Sprites/sprites/Paper.png")
                )
        );
        ImageView paperBackground = new ImageView(mapImage);
        getChildren().add(paperBackground);
        // 在 GameWindow 构造函数的开头或末尾
        getStylesheets().add(getClass().getResource("/css/GameSceneStyle/gameSceneStyle.css").toExternalForm());
        setPrefSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
        getStyleClass().add("game-window");

        // 确保鼠标事件能被正确捕获（不会被下层地图抢走）
        setPickOnBounds(true);

        // ---------- 标题栏（拖动区域） ----------
        HBox titleBar = new HBox();
        titleBar.setMaxHeight(32);
        titleBar.getStyleClass().add("window-title-bar");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("window-title");

        // 占位弹簧（把关闭按钮推到最右边）
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("window-close-btn");
        closeBtn.setOnAction(e -> close());

        titleBar.getChildren().addAll(titleLabel, spacer, closeBtn);

        // ---------- 内容区域（留给子类放游戏画布/按钮） ----------
        contentArea = new StackPane();
        contentArea.getStyleClass().add("window-content");

        // ---------- 组装窗口 ----------
        getChildren().addAll(contentArea, titleBar);
        StackPane.setAlignment(titleBar, Pos.TOP_CENTER);
        StackPane.setAlignment(contentArea, Pos.CENTER);

        // ---------- 事件绑定 ----------
        // 标题栏负责拖动
        titleBar.setOnMousePressed(this::onDragStart);
        titleBar.setOnMouseDragged(this::onDragDragging);

        // 点击窗口任意位置（除标题栏按钮外）将其置顶
        this.setOnMouseClicked(e -> toFront());
    }

    // -------- 拖动逻辑（完全兼容地图缩放和平移） --------
    private void onDragStart(MouseEvent e) {
        dragStartSceneX = e.getSceneX();
        dragStartSceneY = e.getSceneY();
        layoutStartX = getLayoutX();
        layoutStartY = getLayoutY();
        toFront(); // 拖动时自动置顶
        e.consume(); // 防止事件穿透
    }

    private void onDragDragging(MouseEvent e) {
        // 将当前鼠标位置和起始鼠标位置都转换到 mapGroup 的本地坐标系
        // 这样即使地图缩放/平移，拖动的偏移量也是精准的世界坐标偏移
        Point2D currentLocal = mapGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
        Point2D startLocal = mapGroup.sceneToLocal(dragStartSceneX, dragStartSceneY);

        double deltaX = currentLocal.getX() - startLocal.getX();
        double deltaY = currentLocal.getY() - startLocal.getY();

        double newX = layoutStartX + deltaX;
        double newY = layoutStartY + deltaY;

        // 限制窗口不能拖出地图边界（防止窗口掉到地图外面去）
        double maxX = GameSceneCtrl.getWorldWidth() - WIDTH;
        double maxY = GameSceneCtrl.getWorldHeight() - HEIGHT;
        newX = Math.max(0, Math.min(newX, maxX));
        newY = Math.max(0, Math.min(newY, maxY));

        setLayoutX(newX);
        setLayoutY(newY);
        e.consume();
    }

    // -------- 关闭方法 --------
    public void close() {
        mapGroup.getChildren().remove(this);
        gameSceneCtrl.removeWindow(windowId);
        onClosed();
    }
    /**
     * 钩子方法：子类重写以清理资源（例如停止游戏循环）
     */
    protected void onClosed() {
        // 默认空实现
    }

    // -------- 提供给子类的内容区域 ----------
    protected StackPane getContentArea() {
        return contentArea;
    }
}