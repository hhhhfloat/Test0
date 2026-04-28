package GamePage;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Points {
    int x, y, length, height, num;
    Group group = new Group();
    double xCoordinate, yCoordinate;

    public Points(int x, int y, int length, int height, int num) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.num = num;
        xCoordinate = (x + 0.5) * length;
        yCoordinate = (y + 0.5) * height;
    }

    public Group point() {
        Circle circle = new Circle(xCoordinate, yCoordinate, 20, Color.WHITE);
        circle.setStroke(Color.BLACK);
        Label number = new Label(String.valueOf(num));
        number.setLayoutX(xCoordinate);
        number.setLayoutY(yCoordinate);
        group.getChildren().addAll(circle, number);
        group.setOnMouseClicked(event -> {
            Rectangle chooseBox = new Rectangle(x * length + 1.5, y * height + 1.5, length - 3, height - 3);
            chooseBox.setFill(Color.TRANSPARENT);
            chooseBox.setStroke(Color.RED);
            chooseBox.setStrokeWidth(3);

            group.getChildren().add(chooseBox);
            chooseBox.toFront();
            //group.getChildren().clear();
        });
        return group;
    }
}
