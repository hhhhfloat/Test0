package view.game_nodes.Interfaces;

public interface TimeLabelInterface {
    void start();
    void pauseTime();
    void pauseTime(int milliseconds) throws java.lang.InterruptedException;
    void continueTime();
    int getRemainingTime();
    int getTime();
}
