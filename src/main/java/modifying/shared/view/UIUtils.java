package modifying.shared.view;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class UIUtils {

    // 默认缩放倍率和动画时长（可全局调整）
    private static final double DEFAULT_SCALE = 1.08;
    private static final double DEFAULT_DURATION_MS = 180;

    /**
     * 使用默认参数为节点添加悬浮缩放效果
     */
    public static void addHoverScale(Node node) {
        addHoverScale(node, DEFAULT_SCALE, DEFAULT_DURATION_MS);
    }

    /**
     * 自定义缩放倍率，使用默认时长
     */
    public static void addHoverScale(Node node, double targetScale) {
        addHoverScale(node, targetScale, DEFAULT_DURATION_MS);
    }

    /**
     * 自定义缩放倍率和时长（最完整版本）
     */
    public static void addHoverScale(Node node, double targetScale, double durationMs) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(durationMs), node);
        scaleUp.setToX(targetScale);
        scaleUp.setToY(targetScale);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(durationMs), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

        node.setOnMouseEntered(e -> scaleUp.playFromStart());
        node.setOnMouseExited(e -> scaleDown.playFromStart());
    }
    public static void addHoverToAllButtons(Parent root){
        Platform.runLater(() -> {
            root.lookupAll(".button").forEach(node -> {
                if (node instanceof Button) {
                    addHoverScale(node);
                }
            });
        });
    }
}