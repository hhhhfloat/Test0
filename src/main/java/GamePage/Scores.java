package GamePage;

import javafx.scene.control.Label;

public class Scores {
    public int point = 0;
    public Label points() {
        Label points = new Label(String.valueOf(point));
        return points();
    }
}
