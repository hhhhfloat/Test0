package view.game_nodes;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid extends Group{
    public Grid(int x, int y) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Rectangle temp = rectangle(i, j, 50, 50);
                CellNode obj = new CellNode(i, j, 50, 1);
                getChildren().addAll(temp, obj);
            }
        }
    }
    public static Rectangle rectangle(int x, int y, int length, int width) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x * length);
        rectangle.setY(y * width);
        rectangle.setHeight(length);
        rectangle.setWidth(width);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
}
