package GamePage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Test extends Application {

    private double initialWidth;
    private double initialHeight;
    private Scale scaleTransform;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Button button = new Button("缩放按钮");
        root.getChildren().add(button);

        Scene scene = new Scene(root, 800, 600);

        // 保存初始尺寸
        initialWidth = scene.getWidth();
        initialHeight = scene.getHeight();

        // 创建缩放变换
        scaleTransform = new Scale();
        scene.getRoot().getTransforms().add(scaleTransform);

        // 监听场景大小变化
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = newVal.doubleValue() / initialWidth;
            scaleTransform.setX(scaleX);

            // 同步 Y 轴缩放以保持比例
            if (primaryStage.getHeight() > 0) {
                double scaleY = primaryStage.getHeight() / initialHeight;
                scaleTransform.setY(scaleY);
            }
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double scaleY = newVal.doubleValue() / initialHeight;
            scaleTransform.setY(scaleY);

            // 同步 X 轴缩放以保持比例
            if (primaryStage.getWidth() > 0) {
                double scaleX = primaryStage.getWidth() / initialWidth;
                scaleTransform.setX(scaleX);
            }
        });

        primaryStage.setTitle("场景缩放示例");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
