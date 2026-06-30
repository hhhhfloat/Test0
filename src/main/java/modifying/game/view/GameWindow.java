package modifying.game.view;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import modifying.game.controller.GameSceneCtrl;

public abstract class GameWindow extends StackPane {

    protected final Group mapGroup;
    protected final StackPane contentArea;

    private static final double WIDTH = 320;
    private static final double HEIGHT = 240;

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
        setPrefSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
        setStyle(
                "-fx-background-color: #fdf6e3;" +
                        "-fx-border-color: #d5c4a1;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.2, 5, 5);"
        );

        // 确保鼠标事件能被正确捕获（不会被下层地图抢走）
        setPickOnBounds(true);

        // ---------- 标题栏（拖动区域） ----------
        HBox titleBar = new HBox();
        titleBar.setPrefHeight(32);
        titleBar.setStyle(
                "-fx-background-color: #e6d5b8;" +
                        "-fx-border-color: #d5c4a1;" +
                        "-fx-border-width: 0 0 2px 0;" +
                        "-fx-border-radius: 10 10 0 0;" +
                        "-fx-background-radius: 10 10 0 0;"
        );
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new javafx.geometry.Insets(0, 12, 0, 12));

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-family: 'Comic Sans MS';" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: #5a4a3a;"
        );

        // 占位弹簧（把关闭按钮推到最右边）
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #8b7a66;" +
                        "-fx-font-size: 15px;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );
        closeBtn.setOnAction(e -> close());

        titleBar.getChildren().addAll(titleLabel, spacer, closeBtn);

        // ---------- 内容区域（留给子类放游戏画布/按钮） ----------
        contentArea = new StackPane();
        contentArea.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-radius: 0 0 10 10;" +
                        "-fx-background-radius: 0 0 10 10;"
        );

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