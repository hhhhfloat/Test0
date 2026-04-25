package GamePage;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.*;

public class Points {
    int x, y, length, width;

    public Points(int x, int y, int length, int width) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
    }

    public Circle circle() {
        Circle object = new Circle((x + 0.5) * length, (y + 0.5) * width, 20, Color.BLACK);
        object.setStroke(Color.WHITE);
        object.setOnMouseClicked(event -> {
            object.setFill(Color.WHITE);
        });
        return object;
    }
}
