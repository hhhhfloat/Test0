package view.game_nodes.Interfaces;

public interface TimeLabelInterface {
    void start();
    void pauseTime();
    void pauseTime(int milliseconds);
    void continueTime();
    int getRemainingTime();
    int getTime();
}
