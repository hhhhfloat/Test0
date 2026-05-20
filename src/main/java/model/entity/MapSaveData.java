package model.entity;

import java.util.ArrayList;

public class MapSaveData {
    static final int levelNumber = 5;
    private ArrayList<int[][]> map;
    private boolean isPair;
    private int[] score = new int[levelNumber];
    private int[] remainTime = {180,300};
    private int[] eliminated = new int[levelNumber];
    private int loadNumber;
    private int[] bombCount = new int[levelNumber];
    private int[] freezeCount = new int[levelNumber];
    private int[] hintCount = new int[levelNumber];

    public MapSaveData(){}

    public MapSaveData(int loadNumber){
        this.loadNumber = loadNumber;
        map = new ArrayList<>();
        map.add(new int[0][0]);
        map.add(new int[0][0]);
    }

    public int[][] getMap(int mode){
        return map.get(mode);
    }

    public void setMap(int mode, int[][] mp){
        map.set(mode,mp);
    }
    public int getScore(int mode) {
        return score[mode];
    }
    public boolean getIsPair() {
        return isPair;
    }
    public void setScore(int mode, int score) {
        this.score[mode] = score;
    }
    public void setIsPair(boolean isPair) {
        this.isPair = isPair;
    }
    public void setRemainTime(int mode, int remainTime){
        this.remainTime[mode] = remainTime;
    }
    public int getRemainTime(int mode) { return remainTime[mode]; }
    public int getEliminated(int mode) { return eliminated[mode]; }
    public static int getTotal(int mode) {
        return mode == 0 ? 50 : 16;
    }

    public void setBombCount(int mode, int bombCount) {
        this.bombCount[mode] = bombCount;
    }
    public void setEliminated(int mode, int eliminated) {
        this.eliminated[mode] = eliminated;
    }
    public void setFreezeCount(int mode, int freezeCount) {
        this.freezeCount[mode] = freezeCount;
    }

    public int getBombCount(int mode) {
        return bombCount[mode];
    }
    public int getFreezeCount(int mode) {
        return freezeCount[mode];
    }
    public int getHintCount(int mode) {
        return hintCount[mode];
    }
}
