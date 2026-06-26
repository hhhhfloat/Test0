package modifying.game.controller;

import javafx.animation.Transition;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.robot.Robot;
import javafx.util.Duration;

import static java.lang.Math.clamp;

public class CameraController {
    // 边界约束（基于视觉边界实时计算）
    private double minX, maxX, minY, maxY;
    private static final double BOUNCE_DURATION_MS = 300;
    private static final double OVERSHOOT_LIMIT = 60;
    private static final double WORLD_W = GameSceneCtrl.getWorldWidth();
    private static final double WORLD_H = GameSceneCtrl.getWorldHeight();

    private final Node targetNode;          // contentContainer
    private double viewportWidth, viewportHeight;
    private double overshootLimit = OVERSHOOT_LIMIT;
    private boolean wasDragged = false;

    private double mouseAnchorX, mouseAnchorY;
    private double translateAnchorX, translateAnchorY;
    private double currentTx, currentTy;
    private Transition bounceTransition;

    // 缩放状态（仅用于倍数限制，不再用于边界计算）
    private double currentScale = 1.0;
    private Transition snapTransition;
    private static final double MIN_SCALE = 0.2;
    private static final double MAX_SCALE = 2.0;

    private boolean isDragged;

    // 视觉边界提供者
    public interface BoundsProvider {
        Bounds getVisualBounds();
    }
    private BoundsProvider boundsProvider;

    public CameraController(Node targetNode, double viewportWidth, double viewportHeight) {
        this.targetNode = targetNode;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        resetToCenter();
    }

    public void setBoundsProvider(BoundsProvider provider) {
        this.boundsProvider = provider;
        // 立即计算一次边界
        calculateEdge();
    }

    public void setScale(double newScale) {
        if (Math.abs(newScale - currentScale) < 0.0001) return;
        currentScale = newScale;
        // 缩放后，视觉边界已变化，重新计算并修正
        calculateEdge();
        snapToBounds();
    }

    public Node getTargetNode() { return targetNode; }

    private void snapToBounds() {
        double curX = targetNode.getTranslateX();
        double curY = targetNode.getTranslateY();
        currentTx = curX;
        currentTy = curY;

        calculateEdge();
        double endX = clamp(curX, minX, maxX);
        double endY = clamp(curY, minY, maxY);
        if (Math.abs(curX - endX) < 0.01 && Math.abs(curY - endY) < 0.01) return;

        if (snapTransition != null) snapTransition.stop();

        final double startX = curX;
        final double startY = curY;

        snapTransition = new Transition() {
            {
                setCycleDuration(Duration.millis(250));
                setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            }
            @Override
            protected void interpolate(double frac) {
                double currentX = startX + (endX - startX) * frac;
                double currentY = startY + (endY - startY) * frac;
                targetNode.setTranslateX(currentX);
                targetNode.setTranslateY(currentY);
                currentTx = currentX;
                currentTy = currentY;
            }
        };
        snapTransition.setOnFinished(e -> {
            targetNode.setTranslateX(endX);
            targetNode.setTranslateY(endY);
            currentTx = endX;
            currentTy = endY;
            snapTransition = null;
        });
        snapTransition.play();
    }

    public void setTranslate(double x, double y) {
        targetNode.setTranslateX(x);
        targetNode.setTranslateY(y);
        currentTx = x;
        currentTy = y;
    }

    public void attachTo(Node eventSource) {
        eventSource.setOnMousePressed(this::onMousePressed);
        eventSource.setOnMouseDragged(this::onMouseDragged);
        eventSource.setOnMouseReleased(this::onMouseReleased);
    }

    private void onMousePressed(MouseEvent e) {
        wasDragged = false;
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
        }
        if (snapTransition != null) {
            snapTransition.stop();
            snapTransition = null;
        }
        mouseAnchorX = e.getSceneX();
        mouseAnchorY = e.getSceneY();
        translateAnchorX = targetNode.getTranslateX();
        translateAnchorY = targetNode.getTranslateY();
    }

    private void onMouseDragged(MouseEvent e) {
        wasDragged = true;
        double deltaX = e.getSceneX() - mouseAnchorX;
        double deltaY = e.getSceneY() - mouseAnchorY;
        double targetTx = translateAnchorX + deltaX;
        double targetTy = translateAnchorY + deltaY;
        double clampedTx = applyElasticLimit(targetTx, minX, maxX);
        double clampedTy = applyElasticLimit(targetTy, minY, maxY);
        targetNode.setTranslateX(clampedTx);
        targetNode.setTranslateY(clampedTy);
        currentTx = clampedTx;
        currentTy = clampedTy;
    }

    private void onMouseReleased(MouseEvent e) {
        if (wasDragged && isOutOfBounds(currentTx, currentTy)) {
            startBounceAnimation();
        }
        wasDragged = false;
    }

    private double applyElasticLimit(double value, double min, double max) {
        if (value < min) {
            double overshoot = min - value;
            double limitedOvershoot = Math.min(overshootLimit, overshoot * 0.5);
            return min - limitedOvershoot;
        } else if (value > max) {
            double overshoot = value - max;
            double limitedOvershoot = Math.min(overshootLimit, overshoot * 0.5);
            return max + limitedOvershoot;
        } else {
            return value;
        }
    }

    private boolean isOutOfBounds(double x, double y) {
        return x < minX || x > maxX || y < minY || y > maxY;
    }

    private void startBounceAnimation() {
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
        }
        if (snapTransition != null) {
            snapTransition.stop();
            snapTransition = null;
        }
        double startX = targetNode.getTranslateX();
        double startY = targetNode.getTranslateY();
        // 动画过程中持续更新缓存值
        currentTx = startX;
        currentTy = startY;
        double endX = clamp(startX, minX, maxX);
        double endY = clamp(startY, minY, maxY);
        if (startX == endX && startY == endY) return;

        bounceTransition = new Transition() {
            {
                setCycleDuration(Duration.millis(BOUNCE_DURATION_MS));
                setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            }
            @Override
            protected void interpolate(double frac) {
                double curX = startX + (endX - startX) * frac;
                double curY = startY + (endY - startY) * frac;
                targetNode.setTranslateX(curX);
                targetNode.setTranslateY(curY);
                currentTx = curX;
                currentTy = curY;
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

    // 注意：resetToCenter 只重置平移，不改变缩放。
    public void resetToCenter() {
        if (bounceTransition != null) {
            bounceTransition.stop();
            bounceTransition = null;
        }
        if (snapTransition != null) {
            snapTransition.stop();
            snapTransition = null;
        }
        targetNode.setTranslateX(-(WORLD_W-viewportWidth)/2);
        targetNode.setTranslateY(-(WORLD_H-viewportHeight)/2);
        currentTx = -(WORLD_W-viewportWidth)/2;
        currentTy = -(WORLD_H-viewportHeight)/2;
    }

    public void updateViewport(double newVpWidth, double newVpHeight) {
        if (Math.abs(newVpWidth - viewportWidth) < 0.01 &&
                Math.abs(newVpHeight - viewportHeight) < 0.01) {
            return;
        }
        viewportWidth = newVpWidth;
        viewportHeight = newVpHeight;



        calculateEdge();
        if(isOutOfBounds(getTranslateX(),getTranslateY())){
            try {
                snapToBounds();
            } catch (IllegalArgumentException e) {
                fireScrollEvent((Node) boundsProvider);
            }
        }
    }

    private void fireScrollEvent(Node target) {
        // target 应为 GameSelectView
        Robot robot = new Robot();
        Point2D screenPos = robot.getMousePosition();
        Point2D localPos = target.sceneToLocal(screenPos); // 相对于 target (GameSelectView)

        ScrollEvent virtualEvent = new ScrollEvent(
                target,
                target,
                ScrollEvent.SCROLL,
                localPos.getX(),
                localPos.getY(),
                screenPos.getX(),
                screenPos.getY(),
                false, false, false, false,
                false, false,
                0, -0.1,
                0, -0.1,
                ScrollEvent.HorizontalTextScrollUnits.NONE, 0,
                ScrollEvent.VerticalTextScrollUnits.NONE, 0,
                0,
                null
        );
        Event.fireEvent(target, virtualEvent);
    }

    private void calculateEdge() {
        if (boundsProvider == null) return;
        Bounds visual = boundsProvider.getVisualBounds();
        if (visual == null) return;
        double visualMinX = visual.getMinX();
        double visualMaxX = visual.getMaxX();
        double visualMinY = visual.getMinY();
        double visualMaxY = visual.getMaxY();

        // 摄像机平移 translateX/Y 表示contentContainer相对于initial pos的偏移。

        minX =  viewportWidth - visualMaxX;
        maxX =  - visualMinX;
        minY = viewportHeight - visualMaxY;
        maxY = - visualMinY;

        // 如果图像小于视口，可能出现 minX > maxX，此时应允许摄像机居中，我们不做特殊处理，但 clamp 会处理。
    }

    public double getTranslateX() { return targetNode.getTranslateX(); }
    public double getTranslateY() { return targetNode.getTranslateY(); }
    public double getViewportWidth() { return viewportWidth; }
    public double getViewportHeight() { return viewportHeight; }
    public double getCurrentScale() { return currentScale; }
}