package model.entity;

import java.util.ArrayList;

public class MapSaveData {
    private ArrayList<int[][]> map;
    private boolean isPair;
    private int[] score = new int[2];
    private int[] remainTime = {180,300};
    private int[] eliminated = new int[2];
    private int loadNumber;

    public MapSaveData(){}

    public MapSaveData(int loadNumber){
        this.loadNumber = loadNumber;
        map = new ArrayList<>();
        map.add(new int[0][0]);
        map.add(new int[0][0]);
    }

    public MapSaveData(int[][] easyMap, int[][] hardMap, boolean isPair, int easyScore, int hardScore, int loadNumber, int easyTime, int hardTime, int easyEliminated, int hardEliminated)
    {
        map = new ArrayList<>();
        map.add(easyMap);
        map.add(hardMap);
        this.isPair = isPair;
        score[0] = easyScore;
        score[1] = hardScore;
        remainTime[0] = easyTime;
        remainTime[1] = hardTime;
        eliminated[0] = easyEliminated;
        eliminated[1] = hardEliminated;
        this.loadNumber = loadNumber;
    }

    public int[][] getMap(int mode){
        return map.get(mode);
    }

    public void setEliminated(int mode, int eliminated) {
        this.eliminated[mode] = eliminated;
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
}
