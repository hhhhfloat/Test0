package model.entity;

public class MapSaveData {
    private int[][] easyMap;
    private int[][] hardMap;
    private int isPair;

    public MapSaveData(){}

    public MapSaveData(int[][] easyMap, int[][] hardMap, boolean isPair)
    {
        this.easyMap = easyMap;
        this.hardMap = hardMap;
        this.isPair = isPair?2:1;
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
}
