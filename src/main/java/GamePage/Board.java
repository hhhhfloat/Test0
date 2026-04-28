package GamePage;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.Functions.LinkyMap;

public class Board {
    LinkyMap map = new LinkyMap(12, 12, 0);
    int[][] numbers = map.getMap();
    public static Rectangle rectangle(double x, double y, double length, double height) {
        Rectangle rectangle = new Rectangle(x * length, y * height, length, height);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }

    public Group grid(int x, int y, int length, int height) {
        Group panel = new Group();
        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                Rectangle grid = rectangle(i, j, length, height);
                panel.getChildren().add(grid);
                if(numbers[i][j]!=-1){
                    Points point = new Points(i, j, length, height, numbers[i][j]);
                    panel.getChildren().add(point.point());
                }
            }
        }
        panel.setOnMouseClicked(event -> {

        });
        return panel;
    }
}
