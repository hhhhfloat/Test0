package dao;

import model.state.GameSnapshot;

public interface GameSaveDao {
    void saveGame(GameSnapshot snapshot);
    GameSnapshot loadGame();
    boolean hasSave();
    void deleteSave();
}
