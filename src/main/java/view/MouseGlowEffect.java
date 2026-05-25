package view;

import com.sun.scenario.DelayedRunnable;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 鼠标跟随光晕效果工具类
 * 用法：在创建 Scene 后调用 MouseGlowEffect.attach(scene, rootPane)
 */
public class MouseGlowEffect {

    private final Circle glowCircle;
    private final Pane overlayPane;
    private double targetX, targetY;      // 鼠标目标位置
    private double currentX, currentY;    // 光晕当前位置
    private AnimationTimer timer;         // 动画循环
    private final List<Timeline> activeRipples = new ArrayList<>();
    private final List<Animation> activeMeteors = new ArrayList<>();
    private static final Random random = new Random();

    private MouseGlowEffect(Scene scene, Pane rootPane) {
        // 覆盖层（透明，不干扰交互）
        overlayPane = new Pane();
        overlayPane.setMouseTransparent(true);
        overlayPane.setPickOnBounds(false);
        overlayPane.prefWidthProperty().bind(rootPane.widthProperty());
        overlayPane.prefHeightProperty().bind(rootPane.heightProperty());


        // 光晕圆形
        glowCircle = new Circle(50);  // 半径改为50

        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(120, 80, 160, 0.25)),   // 中心暗紫，较低透明度
                new Stop(0.4, Color.rgb(80, 50, 120, 0.15)),
                new Stop(0.7, Color.rgb(40, 20, 70, 0.08)),
                new Stop(1, Color.TRANSPARENT)
        );
        glowCircle.setFill(gradient);
        glowCircle.setMouseTransparent(true);
        DropShadow glowEffect = new DropShadow(BlurType.GAUSSIAN, Color.rgb(70, 40, 100, 0.4), 20, 0.3, 0, 0);
        glowCircle.setEffect(glowEffect);

        overlayPane.getChildren().add(glowCircle);


        // 初始化光晕位置
        currentX = rootPane.getWidth() / 2;
        currentY = rootPane.getHeight() / 2;
        glowCircle.setCenterX(currentX);
        glowCircle.setCenterY(currentY);
        targetX = currentX;
        targetY = currentY;

        // 跟踪鼠标位置，记录目标位置
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            targetX = event.getX();
            targetY = event.getY();
        });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            targetX = event.getX();
            targetY = event.getY();
        });

        // 鼠标点击生成扩散圆圈
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            addRippleEffect(event.getX(), event.getY());
            PauseTransition pause = new PauseTransition(Duration.millis(random.nextInt(1000,5000)));
            pause.setOnFinished(e -> createMeteor());
            pause.play();
        });

        // 动画循环：缓动跟踪
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double factor = 0.12;  // 值越小延迟越明显（0.05 ~ 0.2）
                currentX += (targetX - currentX) * factor;
                currentY += (targetY - currentY) * factor;
                glowCircle.setCenterX(currentX);
                glowCircle.setCenterY(currentY);
            }
        };
        timer.start();


        // 窗口大小改变时重新居中（如果鼠标从未移动）
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (targetX == currentX && targetX == rootPane.getWidth() / 2) {
                currentX = newVal.doubleValue() / 2;
                glowCircle.setCenterX(currentX);
                targetX = currentX;
            }
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (targetY == currentY && targetY == rootPane.getHeight() / 2) {
                currentY = newVal.doubleValue() / 2;
                glowCircle.setCenterY(currentY);
                targetY = currentY;
            }
        });
        rootPane.getChildren().addFirst(overlayPane);
    }
    /**
     * 添加波纹扩散效果（多个圆圈）
     * @param x 圆心 X 坐标
     * @param y 圆心 Y 坐标
     */
    private void addRippleEffect(double x, double y) {
        // 生成 2~3 个不同大小和延迟的波纹
        int count = random.nextInt(1,5);
        for (int i = 0; i < count; i++) {
            double maxRadius = 60 + i * random.nextInt(0,300);  // 60, 85, 110
            double startOpacity = 0.5 - i * 0.1; // 0.5, 0.4, 0.3
            long delayMillis = (long) i * random.nextInt(0,80);           // 0ms, 40ms, 80ms

            Color baseColor;
            if(random.nextBoolean()){
                baseColor = Color.rgb(255, 200, 100, startOpacity);
            }else{
                baseColor = Color.rgb(140,120,220,startOpacity);
            }

            double x_random = x+random.nextInt(-10,10),
                    y_random = y+random.nextInt(-10,10);
            Circle ripple = new Circle(x_random, y_random, 0);
            ripple.setFill(Color.TRANSPARENT);
            ripple.setStroke(baseColor);
            ripple.setStrokeWidth(2.5);
            ripple.setMouseTransparent(true);

            overlayPane.getChildren().add(ripple);

            // 动画：半径扩展，透明度降低，最后移除
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(ripple.radiusProperty(), 0),
                            new KeyValue(ripple.strokeProperty(), baseColor)
                    ),
                    new KeyFrame(Duration.millis(maxRadius * random.nextInt(8,13)),
                            new KeyValue(ripple.radiusProperty(), maxRadius),
                            new KeyValue(ripple.strokeProperty(), Color.TRANSPARENT)
                    )
            );
            timeline.setDelay(Duration.millis(delayMillis));
            timeline.setOnFinished(e -> overlayPane.getChildren().remove(ripple));
            timeline.play();
            activeRipples.add(timeline);
        }
    }

    // 流星动画基准时长（毫秒），控制整体快慢
    private double meteorBaseDurationMs = 1000;
    public void createMeteor(){
        double sceneWidth = overlayPane.getWidth();
        double sceneHeight = overlayPane.getHeight();
        if (sceneWidth <= 0 || sceneHeight <= 0) return;

        // 起始位置：屏幕上半部分 + 右半部分（确保从右上区域开始）
        double startX = sceneWidth * (0.6 + random.nextDouble() * 0.4); // 右侧 60%~100%
        double startY = random.nextDouble() * (sceneHeight * 0.4);       // 上半部分 0~40%

        double angleDeg = 35;
        double tanAngle = Math.tan(Math.toRadians(angleDeg)); // ≈0.7002

        double travelX = sceneWidth * 1.2;   // 向左移动距离
        double dx = -travelX;                // 向左
        double dy = travelX * tanAngle;      // 向下（正值）

        double endX = startX + dx;
        double endY = startY + dy;

        // 使用一条固定长度的线段，整体平移
        double length = 120 + random.nextInt(100);
        double angle = Math.atan2(endY - startY, endX - startX);
        double headX = startX;
        double headY = startY;
        double tailX = headX - length * Math.cos(angle);
        double tailY = headY - length * Math.sin(angle);
        Line meteor = new Line(tailX, tailY, headX, headY);
        meteor.setStartX(tailX);
        meteor.setStartY(tailY);
        meteor.setEndX(headX);
        meteor.setEndY(headY);

        // 流星样式：白线带发光和渐变透明度
        meteor.setStroke(Color.rgb(255, 255, 255, 0.9));
        meteor.setStrokeWidth(2.5);
        meteor.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(200, 200, 255, 0.8), 8, 0.5, 0, 0));
        meteor.setMouseTransparent(true);
        overlayPane.getChildren().add(meteor);
        meteor.setOpacity(0.0);

        Duration moveDuration = Duration.millis(meteorBaseDurationMs);
        Duration fadeInDuration = Duration.millis(meteorBaseDurationMs * 0.4);
        Duration fadeOutDelay = Duration.millis(meteorBaseDurationMs * 0.25);
        Duration fadeOutDuration = Duration.millis(meteorBaseDurationMs * 0.3);

// 动画：平移
        TranslateTransition tt = new TranslateTransition(moveDuration, meteor);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(endX - startX);
        tt.setToY(endY - startY);
        tt.setInterpolator(javafx.animation.Interpolator.LINEAR);

// 淡入
        FadeTransition ftIn = new FadeTransition(fadeInDuration, meteor);
        ftIn.setFromValue(0.0);
        ftIn.setToValue(0.9);

// 淡出（不再设置 setDelay）
        FadeTransition ftOut = new FadeTransition(fadeOutDuration, meteor);
        ftOut.setFromValue(0.9);
        ftOut.setToValue(0.0);

// 顺序播放：先同时移动+淡入，延迟 fadeOutDelay 后开始淡出
        tt.play();
        ftIn.play();

// 延迟后启动淡出
        PauseTransition pause = new PauseTransition(fadeOutDelay);
        pause.setOnFinished(e -> ftOut.play());
        pause.play();

// 在淡出结束后移除节点
        ftOut.setOnFinished(e -> {
            if (meteor.getParent() != null) overlayPane.getChildren().remove(meteor);
        });

// 安全后备：移动结束后如果节点还在（例如淡出被跳过），也移除
        tt.setOnFinished(e -> {
            if (meteor.getParent() != null) overlayPane.getChildren().remove(meteor);
            activeMeteors.remove(tt);
            activeMeteors.remove(ftIn);
            activeMeteors.remove(ftOut);
            activeMeteors.remove(pause);
        });

        activeMeteors.add(tt);      // 记录以便detach
        activeMeteors.add(ftIn);
        activeMeteors.add(ftOut);
        activeMeteors.add(pause);
    }

    public static MouseGlowEffect attach(Scene scene, Pane rootPane) {
        return new MouseGlowEffect(scene, rootPane);
    }

    public void setRadius(double radius) {
        glowCircle.setRadius(radius);
    }

    public void setGradient(RadialGradient gradient) {
        glowCircle.setFill(gradient);
    }

    public void detach() {
        if (timer != null) {
            timer.stop();
        }
        // 停止并清空所有波纹动画
        for (Timeline t : activeRipples) {
            t.stop();
        }
        activeRipples.clear();
        // 停止并清空所有流星相关动画
        for (Animation a : activeMeteors) {
            a.stop();
        }
        activeMeteors.clear();
        if (overlayPane.getParent() != null) {
            ((Pane) overlayPane.getParent()).getChildren().remove(overlayPane);
        }
    }
}