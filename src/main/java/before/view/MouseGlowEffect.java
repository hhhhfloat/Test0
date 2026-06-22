package before.view;

import before.controller.AudioCtrl;
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 鼠标跟随光晕效果工具类
 * 用法：在创建 Scene 后调用 MouseGlowEffect.attach(authScene, rootPane)
 */
public class MouseGlowEffect {

    private static AudioCtrl audioCtrl = null;
    private final Circle glowCircle;
    private final Pane overlayPane;
    private double targetX, targetY;      // 鼠标目标位置
    private double currentX, currentY;    // 光晕当前位置
    private AnimationTimer timer;         // 动画循环
    private final List<Timeline> activeRipples = new ArrayList<>();
    private final List<Animation> activeMeteors = new ArrayList<>();
    private static final Random random = new Random();
    private ArrayDeque<Double> lastClickX = new ArrayDeque<>();
    private ArrayDeque<Double> lastClickY = new ArrayDeque<>();

    public static void setAudioCtrl(AudioCtrl audioCtrl) {
        MouseGlowEffect.audioCtrl = audioCtrl;
    }

    private MouseGlowEffect(Scene scene, Pane rootPane) {
        // 覆盖层（透明，不干扰交互）
        overlayPane = rootPane;

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
            audioCtrl.playToggleSound();
            double x = event.getX();
            double y = event.getY();
            addRippleEffect(x, y);
            lastClickX.addLast(x);
            lastClickY.addLast(y);
            PauseTransition pause = new PauseTransition(Duration.millis(random.nextInt(1000,5000)));
            pause.setOnFinished(e -> {
                createMeteor(lastClickX.pop(), lastClickY.pop());
            });
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
    }
    /**
     * 添加波纹扩散效果（多个圆圈）
     * @param x 圆心 X 坐标
     * @param y 圆心 Y 坐标
     */
    private void addRippleEffect(double x, double y) {
        // 生成 2~4 个不同大小和延迟的波纹
        int count = random.nextInt(2,4);
        for (int i = 0; i < count; i++) {
            double maxRadius = 100 + i * random.nextInt(0,200);  // 60, 85, 110
            double startOpacity = 0.5 - i * 0.1; // 0.5, 0.4, 0.3
            long delayMillis = (long) i * random.nextInt(0,80);           // 0ms, 40ms, 80ms

            Color baseColor;
            Color[] colorPool = {
                    Color.web("#679999"),
                    Color.web("#F4F7F7"),
                    Color.web("#AACFD0"),
                    Color.web("#1F4E5F")
            };
            baseColor = colorPool[random.nextInt(colorPool.length)];

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
                            new KeyValue(ripple.radiusProperty(), 5),
                            new KeyValue(ripple.strokeProperty(), baseColor)
                    ),
                    new KeyFrame(Duration.millis(maxRadius * random.nextInt(8,13) +1000+ random.nextInt(-1000,1000)),
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

    public void createMeteor(double endX,double endY){
        double sceneWidth = overlayPane.getWidth();
        double sceneHeight = overlayPane.getHeight();
        if (sceneWidth <= 0 || sceneHeight <= 0) return;


        double AendX = endX + random.nextDouble(-1,1)*20;
        double AendY = endY+random.nextDouble(-1,1)*20;

        double angleRad = 14.5/18*Math.PI;
        double distance = sceneWidth;
        double startX = AendX-Math.cos(angleRad)*distance;
        double startY = AendY-Math.sin(angleRad)*distance;
        double length = 80+random.nextInt(100);
        double headX = startX;
        double headY = startY;
        double tailX = headX - length * Math.cos(angleRad);
        double tailY = headY - length * Math.sin(angleRad);

        Line meteor = new Line(tailX, tailY, headX, headY);
        meteor.setStartX(tailX);
        meteor.setStartY(tailY);
        meteor.setEndX(headX);
        meteor.setEndY(headY);

        // 流星样式：白线带发光和渐变透明度
        int r = random.nextInt(0,10000);
        if(r == 0)meteor.setStroke(Color.web("#FF6347"));
        else meteor.setStroke(Color.rgb(255, 255, 255, 0.9));

        meteor.setStrokeWidth(2.5);
        meteor.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(200, 200, 255, 0.8), 8, 0.5, 0, 0));
        meteor.setMouseTransparent(true);
        overlayPane.getChildren().add(meteor);
        meteor.setOpacity(0.0);

        // 流星动画基准时长（毫秒），控制整体快慢
        double meteorBaseDurationMs = 600;
        Duration moveDuration = Duration.millis(meteorBaseDurationMs);
        Duration fadeInDuration = Duration.millis(meteorBaseDurationMs * 0.4);
        Duration fadeOutDelay = Duration.millis(meteorBaseDurationMs * 0.7);
        Duration fadeOutDuration = Duration.millis(meteorBaseDurationMs * 0.3);

        TranslateTransition tt = new TranslateTransition(moveDuration, meteor);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(AendX - startX);
        tt.setToY(AendY - startY);
        tt.setInterpolator(javafx.animation.Interpolator.LINEAR);

        FadeTransition ftIn = new FadeTransition(fadeInDuration, meteor);
        ftIn.setFromValue(0.0);
        ftIn.setToValue(0.9);

        FadeTransition ftOut = new FadeTransition(fadeOutDuration, meteor);
        ftOut.setFromValue(0.9);
        ftOut.setToValue(0.0);

        tt.play();
        ftIn.play();

        PauseTransition pause = new PauseTransition(fadeOutDelay);
        pause.setOnFinished(e -> ftOut.play());
        pause.play();

        ftOut.setOnFinished(e -> {
            if (meteor.getParent() != null)
                overlayPane.getChildren().remove(meteor);
            addRippleEffect(AendX,AendY);
            generateParticles(AendX, AendY);
            // 清理动画引用（避免内存泄漏）
            activeMeteors.remove(tt);
            activeMeteors.remove(ftIn);
            activeMeteors.remove(ftOut);
            activeMeteors.remove(pause);
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

    public void generateParticles(double x, double y){
        int particleCount = random.nextInt(5,16);
        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double speed = random.nextInt(150, 400);
            double distance = random.nextInt(60, 180);
            double durationMs = distance / speed * 1000;

            Circle particle = new Circle(x, y, random.nextInt(2, 5));
            particle.setFill(Color.rgb(
                    255,
                    100 + random.nextInt(155),   // 100~255
                    50 + random.nextInt(100),    // 50~150
                    0.9
            ));

            particle.setMouseTransparent(true);
            overlayPane.getChildren().add(particle);

            double deltaX = Math.cos(angle) * distance;
            double deltaY = Math.sin(angle) * distance;

            TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), particle);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.setToX(deltaX);
            tt.setToY(deltaY);
            tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(Duration.millis(durationMs), particle);
            ft.setFromValue(0.9);
            ft.setToValue(0.0);

            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(e -> overlayPane.getChildren().remove(particle));
            pt.play();
            activeMeteors.add(pt);
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
