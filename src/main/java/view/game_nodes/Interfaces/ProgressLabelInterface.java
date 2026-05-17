package view.game_nodes.Interfaces;

public interface ProgressLabelInterface {
    int getEliminated();
    int getTotal();
    int getRemaining();
    String getProgress();
    void eliminate();
}
