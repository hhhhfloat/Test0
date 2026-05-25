package view;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
        rootPane.getChildren().add(overlayPane);
        overlayPane.toBack();

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
    }
    /**
     * 添加波纹扩散效果（多个圆圈）
     * @param x 圆心 X 坐标
     * @param y 圆心 Y 坐标
     */
    private void addRippleEffect(double x, double y) {
        // 生成 2~3 个不同大小和延迟的波纹
        int count = random.nextInt(1,3);
        for (int i = 0; i < count; i++) {
            double maxRadius = 60 + i * 25;  // 60, 85, 110
            double startOpacity = 0.5 - i * 0.1; // 0.5, 0.4, 0.3
            long delayMillis = i * 40L;           // 0ms, 40ms, 80ms

            double x_random = x+random.nextInt(-3,3),
                    y_random = y+random.nextInt(-3,3);
            Circle ripple = new Circle(x_random, y_random, 0);
            ripple.setFill(Color.TRANSPARENT);
            ripple.setStroke(Color.rgb(140, 120, 220, startOpacity));
            ripple.setStrokeWidth(2.5);
            ripple.setMouseTransparent(true);

            overlayPane.getChildren().add(ripple);

            // 动画：半径扩展，透明度降低，最后移除
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(ripple.radiusProperty(), 0),
                            new KeyValue(ripple.strokeProperty(), Color.rgb(140, 120, 220, startOpacity))
                    ),
                    new KeyFrame(Duration.millis(random.nextInt(900,1800)),
                            new KeyValue(ripple.radiusProperty(), maxRadius),
                            new KeyValue(ripple.strokeProperty(), Color.rgb(140, 120, 220, 0))
                    )
            );
            timeline.setDelay(Duration.millis(delayMillis));
            timeline.setOnFinished(e -> overlayPane.getChildren().remove(ripple));
            timeline.play();
            activeRipples.add(timeline);
        }
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
        if (overlayPane.getParent() != null) {
            ((Pane) overlayPane.getParent()).getChildren().remove(overlayPane);
        }
    }
}