package model.state;

import java.time.LocalDateTime;

public class GameSnapshot {
    private int[][] gridData;
    private int score;
    private int remainTime;

    public void setGridData(int[][] gridData) {
        this.gridData = gridData;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
