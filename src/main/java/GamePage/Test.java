package GamePage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) {
        // 情况1：不设置 maxWidth
        Button button1 = new Button("按钮1 - 默认");
        button1.setStyle("-fx-background-color: lightcoral;");

        // 情况2：设置 maxWidth 为无限
        Button button2 = new Button("按钮2 - maxWidth=无限");
        button2.setPrefWidth(30);
        button2.setMaxWidth(Double.MAX_VALUE);

        button2.setStyle("-fx-background-color: lightgreen;");

        VBox vbox = new VBox(10, button1, button2);
        vbox.setPrefWidth(300);
        vbox.setStyle("-fx-border-color: black; -fx-padding: 10;");

        stage.setScene(new Scene(vbox));
        stage.setTitle("maxWidth 对比");
        stage.show();
    }
}
