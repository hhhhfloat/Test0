package model.entity;

public class MapSaveData {
    private int[][] easyMap;
    private int[][] hardMap;

    public MapSaveData(){}

    public MapSaveData(int[][] easyMap, int[][] hardMap)
    {
        this.easyMap = easyMap;
        this.hardMap = hardMap;
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
