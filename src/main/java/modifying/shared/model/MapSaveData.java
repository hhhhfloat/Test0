package modifying.shared.model;

public class MapSaveData {
    private int totalPlayTime;
    private int loadNumber;
    private int[][] map;
    int score;

    public int getScore() {
        return score;
    }

    public int[][] getMap() {
        return map;
    }

    public int getTotalPlayTime() {
        return totalPlayTime;
    }

    public MapSaveData(int k){
        totalPlayTime = 0;
        score = 0;
        loadNumber = k;
        map = new int[0][0];
    }
}
