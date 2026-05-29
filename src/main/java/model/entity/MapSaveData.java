package model.entity;

import java.util.ArrayList;
import java.util.Arrays;

public class MapSaveData {
    private static final int levelNumber = 5;
    private static final int[] totalPairs = {8,18,16,32,50};
    private static final int utilNumber = 3;
    private static final int[] allRemainTime = {30,60,60,120,180};

    private ArrayList<int[][]> map;
    private boolean isPair;
    private int[] score = new int[levelNumber];
    private int[] remainTime = {30,60,60,120,180};
    private int[] eliminated = new int[levelNumber];
    private int[] bombCount = new int[levelNumber];
    private int[] freezeCount = new int[levelNumber];
    private int[] hintCount = new int[levelNumber];

    // 记录本存档的各个关卡历史最高分
    private int[] maxHistoryScore = new int[levelNumber];
    private int maxUnlockedLevel = 0;

    public MapSaveData(){
        map = new ArrayList<>();
        for (int i = 0; i < levelNumber; i++) {
            map.add(new int[0][0]);
        }
        Arrays.fill(bombCount,utilNumber);
        Arrays.fill(freezeCount, utilNumber);
        Arrays.fill(hintCount,utilNumber);

    }

    public static int getLevelNumber() {
        return levelNumber;
    }

    public ArrayList<int[][]> getMap() {
        return map;
    }

    public int getMaxUnlockedLevel() {
        return maxUnlockedLevel;
    }

    public void setMaxUnlockedLevel(int maxUnlockedLevel) {
        this.maxUnlockedLevel = maxUnlockedLevel;
    }

    public int[][] getMap(int currentLevel){
        return map.get(currentLevel);
    }

    public void setMap(int currentLevel, int[][] mp){
        map.set(currentLevel,mp);
    }
    public int getScore(int currentLevel) {
        return score[currentLevel];
    }
    public boolean getIsPair() {
        return isPair;
    }
    public void setScore(int currentLevel, int score) {
        this.score[currentLevel] = score;
    }
    public void setIsPair(boolean isPair) {
        this.isPair = isPair;
    }
    public void setRemainTime(int currentLevel, int remainTime){
        this.remainTime[currentLevel] = remainTime;
    }
    public void setRemainTime(int currentLevel){
        this.remainTime[currentLevel] = allRemainTime[currentLevel];
    }
    public int getRemainTime(int currentLevel) { return remainTime[currentLevel]; }
    public int getEliminated(int currentLevel) { return eliminated[currentLevel]; }
    public static int getTotalPairs(int currentLevel) {
        return totalPairs[currentLevel];
    }

    public void setHintCount(int currentLevel, int hintCount){this.hintCount[currentLevel] = hintCount;}
    public void setBombCount(int currentLevel, int bombCount) {
        this.bombCount[currentLevel] = bombCount;
    }
    public void setEliminated(int currentLevel, int eliminated) {
        this.eliminated[currentLevel] = eliminated;
    }
    public void setFreezeCount(int currentLevel, int freezeCount) {
        this.freezeCount[currentLevel] = freezeCount;
    }

    public int getBombCount(int currentLevel) {
        return bombCount[currentLevel];
    }
    public int getFreezeCount(int currentLevel) {
        return freezeCount[currentLevel];
    }
    public int getHintCount(int currentLevel) {
        return hintCount[currentLevel];
    }

    public int getMaxScore(int currentLevel) {
        return maxHistoryScore[currentLevel];
    }

    public void setMaxScore(int currentLevelx, int maxScore) {
        this.maxHistoryScore[currentLevelx] = maxScore;
    }

    public int[] getMaxHistoryScore() {
        return maxHistoryScore;
    }

}
