package modifying.game.controller;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import modifying.game.view.IGameScene;
import modifying.game.view.implement.GameSelectView;
import modifying.shared.controller.MainController;
import modifying.shared.model.TOAST_TYPE;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GameSceneCtrl {
    // 视口尺寸（逻辑像素，与 Scene 一致）
    private static final double VIEWPORT_WIDTH = 800;
    private static final double VIEWPORT_HEIGHT = 800;
    // 世界地图尺寸
    private static final double WORLD_WIDTH = 3600;
    private static final double WORLD_HEIGHT = 2500;

    public static double getWorldHeight() { return WORLD_HEIGHT; }
    public static double getWorldWidth() { return WORLD_WIDTH; }
    public static double getViewportHeight() { return VIEWPORT_HEIGHT; }
    public static double getViewportWidth() { return VIEWPORT_WIDTH; }

    private final MainController mainController;
    private final Stage gameStage;
    private final StackPane gameRoot;
    private final StackPane contentPane;
    private final Group messageGroup;
    private final StackPane messagePane;
    private final StackPane contentContainer;
    private final Scene gameScene;
    private final Stack<IGameScene> sceneStack = new Stack<>();
    private AnimationTimer gameLoop;
    private final CameraController cameraController;

    // ----- 页面节点管理 -----
    private GameSelectView gameSelectView;  // 大厅节点，由本控制器创建并持有
    private Map<String, IGameScene> sceneCache = new HashMap<>(); // 可选：用于缓存其他场景

    public GameSceneCtrl(Stage gameStage, MainController mainController) {
        this.gameStage = gameStage;
        this.mainController = mainController;

        // 1. 根容器
        gameRoot = new StackPane();
        gameScene = new Scene(gameRoot, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        gameStage.setScene(gameScene);

        // 2. 视口面板（动态尺寸）
        contentPane = new StackPane();
        contentPane.prefWidthProperty().bind(gameScene.widthProperty());
        contentPane.prefHeightProperty().bind(gameScene.heightProperty());
        contentPane.minWidthProperty().bind(gameScene.widthProperty());
        contentPane.minHeightProperty().bind(gameScene.heightProperty());
        contentPane.maxWidthProperty().bind(gameScene.widthProperty());
        contentPane.maxHeightProperty().bind(gameScene.heightProperty());

        gameRoot.getChildren().add(contentPane);

        // 3. 世界容器（固定大小 3600x2500）
        contentContainer = new StackPane();
        contentContainer.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        contentContainer.setMinSize(WORLD_WIDTH, WORLD_HEIGHT);
        contentContainer.setMaxSize(WORLD_WIDTH, WORLD_HEIGHT);
        contentPane.getChildren().add(contentContainer);
        contentPane.setAlignment(Pos.TOP_LEFT);

        // 4. 摄像机
        cameraController = new CameraController(
                contentContainer,
                gameScene.getWidth(), gameScene.getHeight()
        );
        // 窗口尺寸变化时更新摄像机视口
        gameScene.widthProperty().addListener((obs, old, newVal) -> {
            if (newVal.doubleValue() > 0) {
                cameraController.updateViewport(gameScene.getWidth(), newVal.doubleValue());
            }
        });
        gameScene.heightProperty().addListener((obs, old, newVal) -> {
            if (newVal.doubleValue() > 0) {
                cameraController.updateViewport(gameScene.getWidth(), newVal.doubleValue());
            }
        });
        // 初始更新
        cameraController.updateViewport(gameScene.getWidth(), gameScene.getHeight());

        // 5. 消息层（Toast/弹窗，独立缩放）
        messagePane = new StackPane();
        messagePane.setPrefSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        messagePane.setMinSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        messagePane.setMaxSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        messagePane.getStylesheets().add(Paths.get("src","main","resources","css","AuthSceneStyle","confirmStyle.css").toUri().toString());
        messagePane.getStylesheets().add(Paths.get("src","main","resources","css","AuthSceneStyle","toastStyle.css").toUri().toString());
        messagePane.setMouseTransparent(true);
        messageGroup = new Group(messagePane);
        messageGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(() -> {
                    double w = gameScene.getWidth();
                    double h = gameScene.getHeight();
                    if (w <= 0 || h <= 0) return 1.0;
                    return Math.min(w / VIEWPORT_WIDTH, h / VIEWPORT_HEIGHT);
                }, gameScene.widthProperty(), gameScene.heightProperty())
        );
        messageGroup.scaleYProperty().bind(messageGroup.scaleXProperty());
        gameRoot.getChildren().add(messageGroup);

        // 6. 主循环
        initGameLoop();
    }


    private void initGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!sceneStack.isEmpty()) {
                    sceneStack.peek().update(now);
                }
            }
        };
        gameLoop.start();
    }

    // ----- 场景导航方法（对外提供）-----

    /**
     * 显示大厅页面（如果已存在则直接显示，否则创建）
     */
    public void showGameSelectView(GameCtrl gameCtrl) {
        if (gameSelectView == null) {
            // 首次创建
            gameSelectView = new GameSelectView(gameCtrl, cameraController);
            // 设置摄像机边界提供者（如果 GameSelectView 实现了 BoundsProvider）
            // cameraController.setBoundsProvider(gameSelectView); // 已在 GameSelectView 构造中设置
        }
        // 如果栈为空，则直接压入；否则替换顶部或追加
        pushScene(gameSelectView);
    }

    /**
     * 进入某个游戏场景（由 GameCtrl 调用）
     */
    public void enterGameScene(IGameScene gameScene) {
        // 如果已有游戏场景在栈顶，可先出栈
        if (!sceneStack.isEmpty() && !(sceneStack.peek() instanceof GameSelectView)) {
            popScene();
        }
        pushScene(gameScene);
    }

    /**
     * 回到大厅
     */
    public void goBackToLobby() {
        while (!sceneStack.isEmpty() && !(sceneStack.peek() instanceof GameSelectView)) {
            popScene();
        }
        // 确保大厅可见（可能已经被销毁）
        if (gameSelectView != null && !sceneStack.contains(gameSelectView)) {
            pushScene(gameSelectView);
        }
    }

    // ----- 栈操作（内部使用）-----

    private void pushScene(IGameScene newScene) {
        if (!sceneStack.isEmpty()) {
            sceneStack.peek().getView().setDisable(true);
        }

        sceneStack.push(newScene);
        newScene.onEnter();
        contentContainer.getChildren().add(newScene.getView());
        newScene.getView().setDisable(false);
    }

    private void popScene() {
        if (sceneStack.size() <= 1) {
            return;
        }
        IGameScene top = sceneStack.pop();
        top.onExit();
        contentContainer.getChildren().remove(top.getView());
        IGameScene newTop = sceneStack.peek();
        newTop.onResume();
        newTop.getView().setDisable(false);
    }

    // ----- 对外 Getter -----

    public CameraController getCameraController() {
        return cameraController;
    }

    public GameSelectView getGameSelectView() {
        return gameSelectView;
    }

    public StackPane getContentContainer() {
        return contentContainer;
    }

    public void resetCameraToCenter() {
        cameraController.resetToCenter();
    }

    public void popAllScenes() {
        while (sceneStack.size() > 1) {
            popScene();
        }
    }


    private Node currentToast;
    private static final double TOAST_SLIDE_RATIO = 0.6;
    private static final double TOAST_TOP_OFFSET_RATIO = 0.04;
    private static final double TOAST_DISPLAY_SECONDS = 2.5;
    private static final double TOAST_ANIMATION_MILLIS = 400;
    private static final double TOAST_OVERSHOOT_MILLIS_RATIO = 0.5;
    private static final double TOAST_UNDERSHOOT_MILLIS_RATIO = 1.2;
    private static final double TOAST_SETTLE_MILLIS_RATIO = 1.5;
    public void showToast(String message, TOAST_TYPE TYPE) {
        if (messagePane == null) return;
        if (currentToast != null) {
            messagePane.getChildren().remove(currentToast);
            currentToast = null;
        }

        HBox toastBox = new HBox();
        toastBox.setMouseTransparent(true);
        toastBox.getStyleClass().addAll("toast-box", TYPE.getCssClass());
        toastBox.setMaxHeight(Region.USE_PREF_SIZE);

        Label label = new Label(message);
        label.setId("toast-label");
        label.setWrapText(true);

        toastBox.getChildren().add(label);

        // 定位（逻辑坐标固定值）
        double slideDistance = VIEWPORT_WIDTH * TOAST_SLIDE_RATIO;
        double topOffset = VIEWPORT_HEIGHT * TOAST_TOP_OFFSET_RATIO;

        // 添加到 messagePane
        messagePane.getChildren().add(toastBox);
        StackPane.setAlignment(toastBox, Pos.TOP_RIGHT);
        toastBox.setTranslateY(topOffset);
        toastBox.setTranslateX(slideDistance);
        currentToast = toastBox;

        // 动画（完全不变）
        toastBox.setOpacity(0);
        toastBox.setScaleX(0.5);
        toastBox.setScaleY(0.5);

        Timeline popInTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(toastBox.translateXProperty(), slideDistance),
                        new KeyValue(toastBox.opacityProperty(), 0),
                        new KeyValue(toastBox.scaleXProperty(), 0.5),
                        new KeyValue(toastBox.scaleYProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_OVERSHOOT_MILLIS_RATIO),
                        new KeyValue(toastBox.translateXProperty(), 0),
                        new KeyValue(toastBox.opacityProperty(), 1),
                        new KeyValue(toastBox.scaleXProperty(), 1.05),
                        new KeyValue(toastBox.scaleYProperty(), 1.05)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_UNDERSHOOT_MILLIS_RATIO),
                        new KeyValue(toastBox.scaleXProperty(), 0.95),
                        new KeyValue(toastBox.scaleYProperty(), 0.95)
                ),
                new KeyFrame(Duration.millis(TOAST_ANIMATION_MILLIS * TOAST_SETTLE_MILLIS_RATIO),
                        new KeyValue(toastBox.scaleXProperty(), 1.0),
                        new KeyValue(toastBox.scaleYProperty(), 1.0)
                )
        );
        popInTimeline.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(TOAST_DISPLAY_SECONDS));
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(TOAST_ANIMATION_MILLIS), toastBox);
        slideOut.setFromX(0);
        slideOut.setToX(slideDistance);
        slideOut.setOnFinished(e -> {
            messagePane.getChildren().remove(toastBox);
            if (currentToast == toastBox) currentToast = null;
        });

        pause.setOnFinished(e -> slideOut.play());
        pause.play();
    }

    public void showConfirmDialog(String title, String message, Runnable onConfirm, Runnable onCancel) {
        contentPane.setEffect(new GaussianBlur());

        messagePane.setMouseTransparent(false);

        if(currentToast != null){
            messagePane.getChildren().remove(currentToast);
            currentToast = null;
        }

        // 内容面板（样式全部在 CSS 中）
        VBox contentBox = new VBox(20);
        contentBox.getStyleClass().add("confirm-box");
        contentBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setId("confirm-title");

        Label msgLabel = new Label(message);
        msgLabel.setId("confirm-message");
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");
        confirmBtn.getStyleClass().add("confirm-btn-confirm");
        cancelBtn.getStyleClass().add("confirm-btn-cancel");

        // 悬停效果由 CSS 的 :hover 处理，不需要 Java 代码

        HBox buttonBox = new HBox(30, confirmBtn);
        if(onCancel != null) buttonBox.getChildren().add(cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(titleLabel, msgLabel, buttonBox);

        // 覆盖层
        StackPane overlay = new StackPane();
        overlay.getChildren().addAll(contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        messagePane.getChildren().add(overlay);
        overlay.toFront();

        // 按钮事件
        confirmBtn.setOnAction(e -> {
            messagePane.getChildren().remove(overlay);
            messagePane.setMouseTransparent(true);
            contentPane.setEffect(null);
            if (onConfirm != null) onConfirm.run();
        });
        if(onCancel != null){
            cancelBtn.setOnAction(e -> {
                messagePane.getChildren().remove(overlay);
                messagePane.setMouseTransparent(true);
                contentPane.setEffect(null);
                onCancel.run();
            });
        }
    }
}