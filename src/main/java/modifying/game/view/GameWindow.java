package modifying.game.view;

import javafx.animation.*;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import modifying.game.controller.CameraController;
import modifying.game.controller.GameSceneCtrl;
import modifying.shared.resources.ResourceManager;
import modifying.shared.view.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
    private double offsetX, offsetY; // 鼠标相对于窗口左上角的本地坐标偏移

    private final List<Group> backingPaperGroups = new ArrayList<>();
    private final int BACKING_COUNT = 2; // 垫几张纸
    private static final double SCATTER_RADIUS = 12; // 散开幅度
    private static final double SCATTER_ROTATION = 6; // 散开旋转角度（度）
    private static final double STACK_OFFSET = 4; // 收起时偏移像素
    public String getWindowId() {
        return windowId;
    }
    private boolean wasDragged;
    private final double[] scatterOffsetsX;
    private final double[] scatterOffsetsY;
    private final double[] scatterRotations;

    public GameWindow(String title, Group mapGroup, GameSceneCtrl gameSceneCtrl) {
        windowId = title;
        this.mapGroup = mapGroup;
        this.gameSceneCtrl = gameSceneCtrl;

        // ---------- 窗口大小和样式 ----------
        Image paperImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/Sprites/sprites/Paper.png")
                )
        );
        // 1. 创建背景纸片（先添加的在下层）
        scatterOffsetsX = new double[BACKING_COUNT];
        scatterOffsetsY = new double[BACKING_COUNT];
        scatterRotations = new double[BACKING_COUNT];
        initBgPapers(paperImage);

        // 2. 创建主纸片（放在背景纸片上面，但仍在标题栏和内容区域下面）
        ImageView mainPaper = new ImageView(paperImage);
        mainPaper.setMouseTransparent(true);
        mainPaper.setPreserveRatio(false);
        mainPaper.setFitWidth(WIDTH);
        mainPaper.setFitHeight(HEIGHT);

        // 主纸片不需要颜色调整，但可以添加阴影
        Group mainGroup = new Group(mainPaper);
        DropShadow mainShadow = new DropShadow();
        mainShadow.setRadius(8);
        mainShadow.setOffsetX(3);
        mainShadow.setOffsetY(3);
        mainShadow.setColor(Color.rgb(0, 0, 0, 0.6));
        mainGroup.setEffect(mainShadow);
        mainGroup.setCache(true);
        mainGroup.setCacheHint(CacheHint.QUALITY);

        getChildren().add(mainGroup);


        // 在 GameWindow 构造函数的开头或末尾
        getStylesheets().add(getClass().getResource("/css/GameSceneStyle/gameSceneStyle.css").toExternalForm());
        setPrefSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);
        getStyleClass().add("game-window");

        // 确保鼠标事件能被正确捕获（不会被下层地图抢走）
        setPickOnBounds(true);

        Rectangle edgeShadow = new Rectangle(WIDTH, HEIGHT);
        edgeShadow.setMouseTransparent(true);
// 使用线性渐变，从左上到右下，边缘变暗
        LinearGradient gradient = new LinearGradient(
                0, 0, 0.8, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.85, Color.TRANSPARENT),
                new Stop(0.95, Color.rgb(0,0,0,0.05)),
                new Stop(1, Color.rgb(0,0,0,0.1))
        );
        edgeShadow.setFill(gradient);
        getChildren().add(edgeShadow);

        ImageView titleBg = ResourceManager.getInstance()
                .getGameAtlas().createImageView("title_bar.png"); // 替换成你的图片key
        titleBg.setFitWidth(WIDTH);
        titleBg.setFitHeight(50);      // 你画好的高度
        titleBg.setPreserveRatio(false);
        titleBg.setMouseTransparent(true); // 让鼠标事件穿透，不干扰拖动

        // 在 StackPane 中将其固定在顶部中央
        StackPane.setAlignment(titleBg, Pos.TOP_CENTER);

        // 添加到窗口（注意顺序：在 mainPaper 之后，titleBar 之前）
        getChildren().add(titleBg);


        // ---------- 标题栏（拖动区域） ----------
        HBox titleBar = new HBox();
        titleBar.setMaxHeight(32);
        titleBar.getStyleClass().add("window-title-bar");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("window-title");

        // 占位弹簧（把关闭按钮推到最右边）
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button();
        ImageView closeBtnImg = ResourceManager.getInstance().
                getGameAtlas().createImageView("close_btn.png");
        closeBtn.getStyleClass().add("window-close-btn");
        closeBtn.setGraphic(closeBtnImg);
        closeBtn.setOnAction(e -> close());

        UIUtils.addHoverScale(closeBtn,1.15);

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
        titleBar.setOnMouseReleased(e -> {
            if (wasDragged) { // 需要标记是否真的拖拽过，防止点击标题栏也触发散开动画
                animateToScattered();
                wasDragged = false;
            }
        });

        // 点击窗口任意位置（除标题栏按钮外）将其置顶
        this.setOnMouseClicked(e -> toFront());

        windowEnterAnimation();
    }

    private void initBgPapers(Image paperImage){

        generateScatterOffsets();
        for (int i = 0; i < BACKING_COUNT; i++) {
            // 创建 ImageView（仅图像，无效果）
            ImageView backing = new ImageView(paperImage);
            backing.setMouseTransparent(true);
            backing.setPreserveRatio(false);
            backing.setFitWidth(WIDTH);
            backing.setFitHeight(HEIGHT);

            // 颜色调整（变暗）
            ColorAdjust darker = new ColorAdjust();
            darker.setBrightness(-0.04);
            backing.setEffect(darker); // 现在只应用这一个效果

            // 创建 Group 并添加 ImageView
            Group paperGroup = new Group(backing);
            paperGroup.setMouseTransparent(true);

            // 对 Group 应用阴影
            DropShadow shadow = new DropShadow();
            shadow.setRadius(2);
            shadow.setOffsetX(2);
            shadow.setOffsetY(2);
            shadow.setColor(Color.rgb(0, 0, 0, 0.5));
            paperGroup.setEffect(shadow);

            // 对 Group 设置旋转和平移
            double angle = (i - (BACKING_COUNT - 1) / 2.0) * SCATTER_ROTATION;
            double offsetX = (i - (BACKING_COUNT - 1) / 2.0) * SCATTER_RADIUS;
            double offsetY = (i - (BACKING_COUNT - 1) / 2.0) * SCATTER_RADIUS * 0.6;
            paperGroup.setRotate(angle);
            paperGroup.setTranslateX(offsetX);
            paperGroup.setTranslateY(offsetY);

            // 启用高质量缓存（消除锯齿）
            paperGroup.setCache(true);
            paperGroup.setCacheHint(CacheHint.QUALITY);

            backingPaperGroups.add(paperGroup); // 这里仍然保存 ImageView 引用，以便动画操作
            getChildren().add(paperGroup); // 添加 Group 到窗口
        }
    }

    private void windowEnterAnimation() {
        // 初始状态：稍大、稍暗、微透（模拟从镜头前放下）
        this.setScaleX(1.12);
        this.setScaleY(1.12);
        this.setOpacity(0.85);

        // 亮度调整（整体变暗）
        ColorAdjust brightnessAdjust = new ColorAdjust();
        brightnessAdjust.setBrightness(-0.2);
        this.setEffect(brightnessAdjust);

        // 1. 尺寸归位
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(350), this);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_OUT);

        // 2. 透明度归位
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        // 3. 亮度归位（用 Timeline 动画 ColorAdjust 的 brightness 属性）
        // 3. 亮度归位
        // 修改点：在创建 KeyValue 时，将 Interpolator.EASE_OUT 作为第三个参数传入
        KeyValue kv = new KeyValue(
                brightnessAdjust.brightnessProperty(),
                0.0,
                Interpolator.EASE_OUT  // ✅ 插值器在这里指定
        );

        // 注意：第一个 KeyFrame 通常不需要指定插值器，因为它定义了起始值
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(brightnessAdjust.brightnessProperty(), -0.2)
        );
        KeyFrame endFrame = new KeyFrame(Duration.millis(350), kv);

        Timeline brightnessUp = new Timeline(startFrame, endFrame);

        // 并行播放
        ParallelTransition enter = new ParallelTransition(scaleDown, fadeIn, brightnessUp);

        // 动画结束后移除临时效果，恢复干净状态
        enter.setOnFinished(e -> {
            this.setEffect(null);
            // 确保 scale 和 opacity 完全精确
            this.setScaleX(1.0);
            this.setScaleY(1.0);
            this.setOpacity(1.0);
        });

        enter.play();
    }

    // -------- 拖动逻辑（完全兼容地图缩放和平移） --------
    private void onDragStart(MouseEvent e) {
        dragStartSceneX = e.getSceneX();
        dragStartSceneY = e.getSceneY();
        // 不再需要 layoutStartX/Y
        Point2D startLocal = mapGroup.sceneToLocal(dragStartSceneX, dragStartSceneY);
        offsetX = getLayoutX() - startLocal.getX();
        offsetY = getLayoutY() - startLocal.getY();
        toFront();
        animateToStacked();
        wasDragged = true;
        e.consume();
    }

    private void onDragDragging(MouseEvent e) {
        Point2D currentLocal = mapGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
        double newX = currentLocal.getX() + offsetX;
        double newY = currentLocal.getY() + offsetY;

        // 限制窗口不能拖出地图边界
        double maxX = GameSceneCtrl.getWorldWidth() - WIDTH;
        double maxY = GameSceneCtrl.getWorldHeight() - HEIGHT;
        newX = Math.max(0, Math.min(newX, maxX));
        newY = Math.max(0, Math.min(newY, maxY));

        setLayoutX(newX);
        setLayoutY(newY);

        // ---------- 边缘滚动检测（保持不变） ----------
        double mouseSceneX = e.getSceneX();
        double mouseSceneY = e.getSceneY();
        double edgeThreshold = 30;
        double scrollSpeedFactor = 0.5;
        CameraController cam = gameSceneCtrl.getLobbyView().getCameraController();
        if (cam != null) {
            double dx = 0, dy = 0;
            if (mouseSceneX < edgeThreshold) {
                dx = scrollSpeedFactor * (edgeThreshold - mouseSceneX);
            } else if (mouseSceneX > cam.getViewportWidth() - edgeThreshold) {
                dx = -scrollSpeedFactor * (mouseSceneX - (cam.getViewportWidth() - edgeThreshold));
            }
            if (mouseSceneY < edgeThreshold) {
                dy = scrollSpeedFactor * (edgeThreshold - mouseSceneY);
            } else if (mouseSceneY > cam.getViewportHeight() - edgeThreshold) {
                dy = -scrollSpeedFactor * (mouseSceneY - (cam.getViewportHeight() - edgeThreshold));
            }
            cam.translateBy(dx, dy);
        }

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

    private void animateToStacked() {
        for (int i = 0; i < backingPaperGroups.size(); i++) {
            Group g = backingPaperGroups.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.millis(75), g);
            tt.setToX(i * STACK_OFFSET);
            tt.setToY(i * STACK_OFFSET);
            tt.setInterpolator(Interpolator.EASE_OUT);
            RotateTransition rt = new RotateTransition(Duration.millis(75), g);
            rt.setToAngle(0);
            rt.setInterpolator(Interpolator.EASE_OUT);
            new ParallelTransition(tt, rt).play();
        }
    }

    private void animateToScattered() {
        generateScatterOffsets();
        for (int i = 0; i < backingPaperGroups.size(); i++) {
            Group g = backingPaperGroups.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.millis(150), g);
            tt.setToX(scatterOffsetsX[i]);
            tt.setToY(scatterOffsetsY[i]);
            tt.setInterpolator(Interpolator.EASE_OUT);
            RotateTransition rt = new RotateTransition(Duration.millis(150), g);
            rt.setToAngle(scatterRotations[i]);
            rt.setInterpolator(Interpolator.EASE_OUT);
            new ParallelTransition(tt, rt).play();
        }
    }

    private static final double SCATTER_RATIO = 2.0; // 全局散开幅度比例，可调整
    private void generateScatterOffsets() {
        Random rand = new Random();
        for (int i = 0; i < BACKING_COUNT; i++) {
            scatterOffsetsX[i] = (rand.nextDouble() - 0.5) * SCATTER_RADIUS * 2 * SCATTER_RATIO;
            scatterOffsetsY[i] = (rand.nextDouble() - 0.5) * SCATTER_RADIUS * 1.2 * SCATTER_RATIO;
            scatterRotations[i] = (rand.nextDouble() - 0.5) * SCATTER_ROTATION * 2 * SCATTER_RATIO;
        }
    }



}