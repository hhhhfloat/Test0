package GamePage.Labels;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ScoreLabel {
    int points;

    public ScoreLabel(int points) {
        this.points = points;
    }

    public Label getPointsLabel() {
        Label pointsLabel = new Label("Total Scores: "+String.valueOf(points));
        pointsLabel.setFont(Font.font(30));
        return pointsLabel;
    }
}
