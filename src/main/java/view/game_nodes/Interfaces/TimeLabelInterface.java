package view.game_nodes.Interfaces;

public interface TimeLabelInterface {
    void start();
    void pauseTime();
    void continueTime();
    int getRemainingTime();
    int getTime();
}
