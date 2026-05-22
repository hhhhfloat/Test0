package view.game_nodes.Interfaces;

public interface TimeLabelInterface {
    void start();
    void pauseTime();
    void pauseTime(int seconds);
    void continueTime();
    int getRemainingTime();
    int getTime();
}
