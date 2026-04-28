package dao;

import model.entity.Account;
import model.state.ScoreEntry;

import java.util.List;

public interface UserDao {
    boolean validate(String username, String password);
    boolean createUser(String username, String password);
    int getHighScore(String username);
    void updateHighScore(String username, int score);
    List<ScoreEntry> getLeaderboard(int limit);
    Account findByUsername(String username);
}
