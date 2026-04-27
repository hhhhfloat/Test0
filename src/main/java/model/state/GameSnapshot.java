package model.state;

import java.time.LocalDateTime;

// 自定义快照——棋盘状态、分数、时间
public class GameSnapshot {
    private int[][] girdData;
    private int currentScore;
    private int remainTime;
    private boolean isPaused;
    private int levelNum;
    private LocalDateTime saveTime;


}
