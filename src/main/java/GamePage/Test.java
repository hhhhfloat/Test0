package GamePage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.stage.Stage;

interface DataProcessor {
    void process(String data);
}

// 使用接口作为参数
class DataSender {
    public void sendData(String data, DataProcessor processor) {
        // 处理数据
        String processedData = "处理: " + data;
        // 通过接口回调传递数据
        processor.process(processedData);
    }
}

// 实现接口
public class Test {
    public static void main(String[] args) {
        DataSender sender = new DataSender();

        // 方式1：匿名内部类
        sender.sendData("Hello", new DataProcessor() {
            @Override
            public void process(String data) {
                System.out.println("收到: " + data);
            }
        });

        // 方式2：Lambda表达式（Java 8+）
        sender.sendData("World", data -> System.out.println("收到: " + data));
    }
}
