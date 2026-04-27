package GamePage;

import ButtonToBeChanged.GameButtons.Pause;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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

    public static class GameScene {
        public static Scene getGameScene() {
            VBox gameboard = new VBox(Grid.grid(10, 10, 50, 50));
            int points = 0;
            ScoreLabel scoreLabel = new ScoreLabel(points);
            TimerLabel timerLabel = new TimerLabel();
            gameboard.getChildren().addAll(Pause.getButton(), scoreLabel.getPointsLabel(), timerLabel.timer());
            return new Scene(gameboard);
        }
    }
}
