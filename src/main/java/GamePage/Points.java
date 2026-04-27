package GamePage;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Points {
    int x, y, length, width, num;

    public Points(int x, int y, int length, int width, int num) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.num = num;
    }

    public Group point() {
        Group group = new Group();
        double xCoordinate = (x + 0.5) * length, yCoordinate = (y + 0.5) * width;
        Circle circle = new Circle(xCoordinate, yCoordinate, 20, Color.WHITE);
        circle.setStroke(Color.BLACK);
        Label number = new Label(String.valueOf(num));
        number.setLayoutX(xCoordinate);
        number.setLayoutY(yCoordinate);
        group.getChildren().addAll(circle,number);
        group.setOnMouseClicked(event -> {
            group.getChildren().clear();
        });
        return group;
    }
}
