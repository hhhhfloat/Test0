package modifying.auth.dao;

import modifying.auth.model.Account;
import modifying.auth.model.ScoreEntry;

import java.util.List;

public interface UserDao {
    boolean existForRegister(String username);
    boolean validate(String username, String password);
    void createUser(String username, String password);
    int getHighScore(String username);
    void updateHighScore(String username, int score);
    List<ScoreEntry> getLeaderboard(int limit);
    Account findByUsername(String username);
    void deleteAccount(String username);
    boolean existForLogin(String username);
}
