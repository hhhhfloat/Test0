package model.entity;

public class MapSaveData {
    private int[][] easyMap;
    private int[][] hardMap;
    private boolean isPair;
    private int easyScore, hardScore;
    private int loadNumber;

    public MapSaveData(){}

    public MapSaveData(int loadNumber){
        this.loadNumber = loadNumber;
    }

    public MapSaveData(int[][] easyMap, int[][] hardMap, boolean isPair, int easyScore, int hardScore, int loadNumber)
    {
        this.easyMap = easyMap;
        this.hardMap = hardMap;
        this.isPair = isPair;
        this.easyScore = easyScore;
        this.hardScore = hardScore;
        this.loadNumber = loadNumber;
    }
    public int[][] getEasyMap(){return easyMap;}
    public void setEasyMap(int[][] easyMap) {
        this.easyMap = easyMap;
    }
    public int[][] getHardMap() {
        return hardMap;
    }
    public void setHardMap(int[][] hardMap) {
        this.hardMap = hardMap;
    }
    public int getEasyScore() {
        return easyScore;
    }
    public int getHardScore() {
        return hardScore;
    }
    public boolean getIsPair() {
        return isPair;
    }
    public void setEasyScore(int easyScore) {
        this.easyScore = easyScore;
    }
    public void setHardScore(int hardScore) {
        this.hardScore = hardScore;
    }
    public void setIsPair(boolean isPair) {
        this.isPair = isPair;
    }

}
