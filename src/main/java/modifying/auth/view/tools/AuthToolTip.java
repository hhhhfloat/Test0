package modifying.auth.view.tools;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class AuthToolTip {

    private final Label tooltipLabel;
    private final Pane parentPane;
    private String currentText = ""; // 🔥 存储当前显示的文本

    public AuthToolTip(Pane parentPane) {
        this.parentPane = parentPane;
        this.tooltipLabel = new Label();

        StackPane.setAlignment(tooltipLabel, Pos.TOP_LEFT);
        tooltipLabel.setTranslateX(0);
        tooltipLabel.setTranslateY(0);
        tooltipLabel.setMouseTransparent(true);
        tooltipLabel.getStyleClass().add("auth-tooltip-label");
        tooltipLabel.setVisible(false);
        parentPane.getChildren().add(tooltipLabel);
    }

    public void install(Node node, String text) {
        this.currentText = text; // 🔥 保存初始文本

        node.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            tooltipLabel.setText(currentText); // 🔥 使用 currentText
            updatePosition(e.getSceneX(), e.getSceneY());
            tooltipLabel.setVisible(true);
            tooltipLabel.toFront();
        });

        node.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            if (tooltipLabel.isVisible()) {
                updatePosition(e.getSceneX(), e.getSceneY());
                tooltipLabel.toFront();
            }
        });

        node.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            tooltipLabel.setVisible(false);
        });
    }

    public void updateText(String newText) {
        this.currentText = newText; // 🔥 更新存储的文本
        // 如果当前是可见状态，立即刷新显示
        if (tooltipLabel.isVisible()) {
            tooltipLabel.setText(newText);
        }
        System.out.println("AuthToolTip updateText: " + newText);
    }

    private void updatePosition(double sceneX, double sceneY) {
        Point2D local = parentPane.sceneToLocal(sceneX, sceneY);

        double offsetX = 15;
        double offsetY = 15;

        double x = local.getX() + offsetX;
        double y = local.getY() + offsetY;

        if (x + tooltipLabel.getWidth() > parentPane.getWidth()) {
            x = local.getX() - tooltipLabel.getWidth() - 10;
        }
        if (y + tooltipLabel.getHeight() > parentPane.getHeight()) {
            y = local.getY() - tooltipLabel.getHeight() - 10;
        }

        tooltipLabel.setTranslateX(x);
        tooltipLabel.setTranslateY(y);
    }
}