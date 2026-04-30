package view.labels;

import javafx.scene.control.Label;

public class ScoreLabel {
    int points;

    public ScoreLabel(int points) {
        this.points = points;
    }

    public Label getPointsLabel() {
        Label pointsLabel = new Label(String.valueOf(points));
        return pointsLabel;
    }
}
