package dao;

import model.state.ScoreEntry;

import java.util.List;

public interface LeaderboardDao {
    void addScore(ScoreEntry entry);
    List<ScoreEntry> getTopScores(int limit);
}
