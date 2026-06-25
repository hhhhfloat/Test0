package modifying.game.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import static java.lang.Math.clamp;

public class CameraController {
    // 正常边界（硬边界）
    private double minX, maxX, minY, maxY;
    // 弹性 overscroll 限制（超出边界的最大像素）
    private static final double OVERSHOOT_LIMIT = 80;
    // 回弹动画时长（毫秒）
    private static final double BOUNCE_DURATION_MS = 300;

    private final Node targetNode;
    private final double worldWidth;
    private final double worldHeight;
    private double viewportWidth;
    private double viewportHeight;

    // 拖拽状态
    private double mouseAnchorX, mouseAnchorY;
    private double translateAnchorX, translateAnchorY;
    // 当前平移值（用于动画）
    private double currentTx, currentTy;

    // 回弹动画
    private Transition bounceTransition;

    public CameraController(Node targetNode, double worldWidth, double worldHeight,
                            double viewportWidth, double viewportHeight) {
        this.targetNode = targetNode;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;

        // 计算正常边界
        minX = -worldWidth/2;
        maxX = worldWidth/2;
        minY = -worldHeight/2;
        maxY = worldHeight/2;

        // 初始化当前值
        resetToCenter();
    }

    public void attachTo(Node eventSource) {
        eventSource.setOnMousePressed(this::onMousePressed);
        eventSource.setOnMouseDragged(this::onMouseDragged);
        eventSource.setOnMouseReleased(this::onMouseReleased);
    }

    private void onMousePressed(MouseEvent e) {
        // 如果正在回弹动画，强制结束并跳到最终位置
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
            // 将节点固定到当前实际位置（避免跳变）
        }

        mouseAnchorX = e.getSceneX();
        mouseAnchorY = e.getSceneY();
        // 记录起始平移（用节点当前的 translate）
        translateAnchorX = targetNode.getTranslateX();
        translateAnchorY = targetNode.getTranslateY();
    }

    private void onMouseDragged(MouseEvent e) {
        double deltaX = e.getSceneX() - mouseAnchorX;
        double deltaY = e.getSceneY() - mouseAnchorY;

        double targetTx = translateAnchorX + deltaX;
        double targetTy = translateAnchorY + deltaY;

        // 应用弹性限制（允许超出边界但施加阻力）
        double clampedTx = applyElasticLimit(targetTx, minX, maxX);
        double clampedTy = applyElasticLimit(targetTy, minY, maxY);

        targetNode.setTranslateX(clampedTx);
        targetNode.setTranslateY(clampedTy);

        // 记录当前值（用于回弹）
        currentTx = clampedTx;
        currentTy = clampedTy;
    }

    private void onMouseReleased(MouseEvent e) {
        // 检查是否超出正常边界，如果是则启动回弹动画
        if (isOutOfBounds(currentTx, currentTy)) {
            startBounceAnimation();
        }
    }

    // ====== 弹性限制算法 ======
    private double applyElasticLimit(double value, double min, double max) {
        if (value < min) {
            double overshoot = min - value;
            // 施加弹性阻力：超出越多，允许增加越少（对数衰减）
            double limitedOvershoot = Math.min(OVERSHOOT_LIMIT, overshoot * 0.5);
            return min - limitedOvershoot;
        } else if (value > max) {
            double overshoot = value - max;
            double limitedOvershoot = Math.min(OVERSHOOT_LIMIT, overshoot * 0.5);
            return max + limitedOvershoot;
        } else {
            return value;
        }
    }

    private boolean isOutOfBounds(double x, double y) {
        return x < minX || x > maxX || y < minY || y > maxY;
    }

    // ====== 回弹动画 ======
    private void startBounceAnimation() {
        if (bounceTransition != null) {
            bounceTransition.stop();
        }

        double startX = targetNode.getTranslateX();
        double startY = targetNode.getTranslateY();
        double endX = clamp(startX, minX, maxX);
        double endY = clamp(startY, minY, maxY);

        // 如果已经在边界内，不需要动画
        if (startX == endX && startY == endY) return;

        bounceTransition = new Transition() {
            {
                setCycleDuration(Duration.millis(BOUNCE_DURATION_MS));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                double curX = startX + (endX - startX) * frac;
                double curY = startY + (endY - startY) * frac;
                targetNode.setTranslateX(curX);
                targetNode.setTranslateY(curY);
                // 更新当前值（以备后续）
                if (frac >= 1.0) {
                    currentTx = endX;
                    currentTy = endY;
                    bounceTransition = null;
                }
            }
        };

        bounceTransition.setOnFinished(e -> {
            targetNode.setTranslateX(endX);
            targetNode.setTranslateY(endY);
            currentTx = endX;
            currentTy = endY;
            bounceTransition = null;
        });

        bounceTransition.play();
    }

    // ====== 公共方法 ======
    public void resetToCenter() {
        double cx = (viewportWidth - worldWidth) / 2;
        double cy = (viewportHeight - worldHeight) / 2;
        double endX = clamp(cx, minX, maxX);
        double endY = clamp(cy, minY, maxY);
        targetNode.setTranslateX(endX);
        targetNode.setTranslateY(endY);
        currentTx = endX;
        currentTy = endY;
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
        }
    }

    public void updateViewport(double newVpWidth, double newVpHeight){
        // 如果尺寸没变，直接返回（避免重复计算）
        if (Math.abs(newVpWidth - viewportWidth) < 0.01 && Math.abs(newVpHeight - viewportHeight) < 0.01) {
            return;
        }
        // 更新视口尺寸
        viewportWidth = newVpWidth;
        viewportHeight = newVpHeight;

        // 重新计算边界
        minX = -worldWidth/2;
        maxX = worldWidth/2;
        minY = -worldHeight/2;
        maxY = worldHeight/2;

        // 调整当前平移位置，确保不超出新边界
        double curX = targetNode.getTranslateX();
        double curY = targetNode.getTranslateY();
        double clampedX = clamp(curX, minX, maxX);
        double clampedY = clamp(curY, minY, maxY);
        if (clampedX != curX || clampedY != curY) {
            targetNode.setTranslateX(clampedX);
            targetNode.setTranslateY(clampedY);
            currentTx = clampedX;
            currentTy = clampedY;
        }

        // 如果当前存在回弹动画，也需要根据新边界重新定位？但更简单：如果正在回弹则停止并立即固定到边界内
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
            targetNode.setTranslateX(clampedX);
            targetNode.setTranslateY(clampedY);
            currentTx = clampedX;
            currentTy = clampedY;
        }
    }

    public double getTranslateX() { return targetNode.getTranslateX(); }
    public double getTranslateY() { return targetNode.getTranslateY(); }
}