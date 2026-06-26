package modifying.game.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import modifying.game.view.IGameScene;
import modifying.game.view.implement.GameSelectView;
import modifying.shared.controller.MainController;

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
}