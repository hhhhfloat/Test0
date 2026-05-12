package view.game_nodes.Labels;

import javafx.scene.control.Label;
import view.game_nodes.Interfaces.ScoreLabelInterface;

public class ScoreLabel extends Label implements ScoreLabelInterface {
    private int score;

    public ScoreLabel() {
        super();
        setText("Score: " + score);
    }

    @Override
    public void addScore(int combo) {
        score += 10 + 5 * (combo - 1);
        setText("Score: " + score);
    }

    @Override
    public int getScore() {
        return score;
    }
}
