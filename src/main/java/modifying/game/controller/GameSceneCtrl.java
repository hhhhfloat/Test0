package modifying.game.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
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

public class GameSceneCtrl{
    // 视口尺寸（逻辑像素，与 Scene 一致）
    private static final double VIEWPORT_WIDTH = 800;
    private static final double VIEWPORT_HEIGHT = 800;
    // 世界地图尺寸（你的底图尺寸）
    private static final double WORLD_WIDTH = 3600;
    private static final double WORLD_HEIGHT = 2500;

    public static double getWorldHeight() {
        return WORLD_HEIGHT;
    }

    public static double getWorldWidth() {
        return WORLD_WIDTH;
    }

    public static double getViewportHeight() {
        return VIEWPORT_HEIGHT;
    }

    public static double getViewportWidth() {
        return VIEWPORT_WIDTH;
    }

    private final MainController mainController;
    private final Stage gameStage;


    private final StackPane gameRoot;
    private final StackPane contentPane;
    private final Group messageGroup;
    private final StackPane messagePane;
    private final StackPane contentContainer;
    private final Scene gameScene;
    private final Map<String, Parent> viewCache = new HashMap<>();

    private GameSelectView gameSelectView;
    private final Stack<IGameScene> sceneStack = new Stack<>();
    private AnimationTimer gameLoop;
    private IGameScene currentScene;
    private CameraController cameraController;
    public GameSceneCtrl(Stage gameStage, MainController mainController) {
        this.gameStage = gameStage;
        this.mainController = mainController;

        // 1. 根容器
        gameRoot = new StackPane();
        gameScene = new Scene(gameRoot, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        gameStage.setScene(gameScene);

        /// 2. 创建视口面板（动态尺寸，与 Scene 一致）
        contentPane = new StackPane();
        // 绑定尺寸到 gameScene 的宽高
        contentPane.prefWidthProperty().bind(gameScene.widthProperty());
        contentPane.prefHeightProperty().bind(gameScene.heightProperty());
        contentPane.minWidthProperty().bind(gameScene.widthProperty());
        contentPane.minHeightProperty().bind(gameScene.heightProperty());
        contentPane.maxWidthProperty().bind(gameScene.widthProperty());
        contentPane.maxHeightProperty().bind(gameScene.heightProperty());

        gameRoot.getChildren().add(contentPane);

        // 3. 创建世界容器（放置所有场景，大小为 3600x2500）
        contentContainer = new StackPane();
        contentContainer.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        contentContainer.setMinSize(WORLD_WIDTH, WORLD_HEIGHT);
        contentContainer.setMaxSize(WORLD_WIDTH, WORLD_HEIGHT);
        // 初始居中（让地图中心出现在视口中心）
        contentPane.getChildren().add(contentContainer);

        cameraController = new CameraController(
                contentContainer,
                WORLD_WIDTH, WORLD_HEIGHT,
                gameScene.getWidth(), gameScene.getHeight()
        );
        // 添加尺寸监听器（直接在这里绑定）
        gameScene.widthProperty().addListener((obs, old, newVal) -> {
            if (newVal.doubleValue() > 0) {
                cameraController.updateViewport(newVal.doubleValue(), gameScene.getHeight());
            }
        });
        gameScene.heightProperty().addListener((obs, old, newVal) -> {
            if (newVal.doubleValue() > 0) {
                cameraController.updateViewport(gameScene.getWidth(), newVal.doubleValue());
            }
        });
        // 触发一次初始更新（确保摄像机边界与当前视口一致）
        cameraController.updateViewport(gameScene.getWidth(), gameScene.getHeight());

        // 4. 创建消息层（Toast/弹窗，独立缩放，放在最上层）
        messagePane = new StackPane();
        messagePane.setPrefSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        messagePane.setMouseTransparent(true);
        messageGroup = new Group(messagePane);
        // 绑定窗口缩放（保持消息层始终在可见区域）
        messageGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(() -> {
                    double w = gameScene.getWidth();
                    double h = gameScene.getHeight();
                    if (w <= 0 || h <= 0) return 1.0;
                    return Math.min(w / VIEWPORT_WIDTH, h / VIEWPORT_HEIGHT);
                }, gameScene.widthProperty(), gameScene.heightProperty())
        );
        messageGroup.scaleYProperty().bind(messageGroup.scaleXProperty());
        // 消息层放在最上层（在 contentPane 之上）
        gameRoot.getChildren().add(messageGroup);

        // 启动主循环
        initGameLoop();
    }

    // 外部获取摄像机（如果需要暴露，比如给 GameSelectView 用于 attachTo）
    public CameraController getCameraController() {
        return cameraController;
    }

    // 重置摄像机到居中位置（对外提供）
    public void resetCameraToCenter() {
        cameraController.resetToCenter();
    }

    private void initGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!sceneStack.isEmpty()) {
                    // 核心：只更新栈顶
                    sceneStack.peek().update(now);
                }
            }
        };
        gameLoop.start();
    }

    public void centerContent(){
        // 初始时让地图居中显示
        double offsetX = (800 - WORLD_WIDTH) / 2;
        double offsetY = (800 - WORLD_HEIGHT) / 2;
        contentPane.setTranslateX(offsetX);
        contentPane.setTranslateY(offsetY);
    }

    // 入栈（替代 showGameSelectScene）
    public void pushScene(IGameScene newScene) {
        // 暂停当前栈顶（如果有）
        if (!sceneStack.isEmpty()) {
            sceneStack.peek().getView().setDisable(true);
        }

        // 场景直接添加到 contentContainer，且 contentContainer 大小为 MAP_WIDTH x MAP_HEIGHT
        sceneStack.push(newScene);
        newScene.onEnter();
        contentContainer.getChildren().add(newScene.getView());
        newScene.getView().setDisable(false);
    }

    // 出栈
    public void popScene() {
        // 保留大厅（栈底），如果只剩 1 个则不允许弹出
        if (sceneStack.size() <= 1) {
            return;
        }

        // 1. 移除并销毁顶层
        IGameScene top = sceneStack.pop();
        top.onExit();
        contentContainer.getChildren().remove(top.getView());

        // 2. 恢复新栈顶的生命周期和交互
        IGameScene newTop = sceneStack.peek();
        newTop.onResume();
        newTop.getView().setDisable(false);
    }

    /**
     * 清空栈并重置（可选的全局退出功能）
     */
    public void popAllScenes() {
        while (sceneStack.size() > 1) {
            popScene();
        }
    }

    public StackPane getContentContainer() {
        return contentContainer;
    }
}
