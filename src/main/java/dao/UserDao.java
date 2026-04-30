package dao;

import model.entity.Account;
import model.state.ScoreEntry;

import java.util.List;

public interface UserDao {
    boolean exist(String username);
    boolean validate(String username, String password);
    void createUser(String username, String password);
    int getHighScore(String username);
    void updateHighScore(String username, int score);
    List<ScoreEntry> getLeaderboard(int limit);
    Account findByUsername(String username);
}
