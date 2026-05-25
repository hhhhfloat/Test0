package view;

import javafx.animation.AnimationTimer;
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

// 阴影效果也调暗，降低发光强度
        DropShadow glowEffect = new DropShadow(BlurType.GAUSSIAN, Color.rgb(70, 40, 100, 0.4), 20, 0.3, 0, 0);
        glowCircle.setEffect(glowEffect);

        overlayPane.getChildren().add(glowCircle);
        rootPane.getChildren().add(overlayPane);
        overlayPane.toBack();

        currentX = rootPane.getWidth() / 2;
        currentY = rootPane.getHeight() / 2;
        glowCircle.setCenterX(currentX);
        glowCircle.setCenterY(currentY);
        targetX = currentX;
        targetY = currentY;

        // 使用事件过滤器，避免覆盖其他处理器（并且可以持续跟踪）
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            targetX = event.getX();
            targetY = event.getY();
        });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            targetX = event.getX();
            targetY = event.getY();
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