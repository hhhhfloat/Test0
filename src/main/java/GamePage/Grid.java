package GamePage;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid {
    public static Rectangle rectangle(double x, double y, double length, double width) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x * length);
        rectangle.setY(y * width);
        rectangle.setHeight(length);
        rectangle.setWidth(width);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }

    public static Group grid(int x, int y, int length, int width) {
        Group panel = new Group();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Rectangle temp = rectangle(i, j, length, width);
                Points point = new Points(i, j, length, width);
                panel.getChildren().addAll(temp, point.circle());

            }
        }
        return panel;
    }
}
