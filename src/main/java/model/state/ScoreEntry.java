package model.state;
// 用户名、分数、日期
public class ScoreEntry {
    private int score;
    private String name;
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public int getScore() {
        return score;
    }
}
