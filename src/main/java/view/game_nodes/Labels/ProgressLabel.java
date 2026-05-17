package view.game_nodes.Labels;

import javafx.scene.control.Label;
import view.game_nodes.Interfaces.ProgressLabelInterface;

public class ProgressLabel extends Label implements ProgressLabelInterface{
    private final int total;
    private int eliminated;

    public ProgressLabel(int eliminated, int total) {
        this.total = total;
        this.eliminated = eliminated;
        setText(getProgress());
    }

    public void eliminate() {
        eliminated++;
        setText(getProgress());
    }

    public int getTotal() {
        return total;
    }

    public int getEliminated() {
        return eliminated;
    }

    public int getRemaining()  {
        return total - eliminated;
    }

    public String getProgress() {
        double progress = (double) eliminated / total;
        return String.format("%.2f %%", progress);
    }
}
