package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleFpsHUD {
    private Stage hudStage;
    private Label fpsLabel;

    public SimpleFpsHUD(Stage owner) {
        // 创建显示文字的标签
        fpsLabel = new Label("FPS: --");
        Path cssPath = Paths.get("src","main","resources","css","fpsStyle.css");
        String cssUri = cssPath.toUri().toString();
        fpsLabel.getStylesheets().add(cssUri);
        // 透明根容器
        StackPane root = new StackPane(fpsLabel);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);  // 场景透明

        hudStage = new Stage(StageStyle.TRANSPARENT); // 窗口透明
        hudStage.initOwner(owner);          // 跟随主窗口
        hudStage.setAlwaysOnTop(true);      // 置顶
        hudStage.setScene(scene);
        hudStage.show();

        // 位置更新函数（左下角）
        Runnable updatePosition = () -> {
            if (owner.isShowing() && hudStage.isShowing()) {
                double hudHeight = hudStage.getHeight();
                if (hudHeight <= 0) {
                    hudHeight = 30; // 预估默认高度
                }
                hudStage.setX(owner.getX() + 30);
                hudStage.setY(owner.getY() + owner.getHeight() - hudHeight - 10);
            }
        };

// 监听所有可能改变位置的因素，统一调用 updatePosition
        owner.xProperty().addListener((o, old, val) -> updatePosition.run());
        owner.yProperty().addListener((o, old, val) -> updatePosition.run());
        owner.heightProperty().addListener((o, old, val) -> updatePosition.run());
        hudStage.heightProperty().addListener((o, old, val) -> updatePosition.run());

        updatePosition.run();

        // 帧率监控
        new AnimationTimer() {
            private long lastReport = System.nanoTime();
            private int frames = 0;
            @Override
            public void handle(long now) {
                frames++;
                if (now - lastReport >= 1_000_000_000) {
                    double fps = frames * 1_000_000_000.0 / (now - lastReport);
                    fpsLabel.setText("FPS: "+fps);
                    frames = 0;
                    lastReport = now;
                }
            }
        }.start();
    }
}