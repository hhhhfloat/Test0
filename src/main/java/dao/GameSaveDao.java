package dao;

import model.state.GameSnapshot;

import java.util.List;

public interface GameSaveDao {
    void saveGame(String username, int slotIndex, GameSnapshot snapshot);
    GameSnapshot loadGame(String username, int slotIndex);
    boolean hasSave(String username, int slotIndex);
    List<Integer> getAvailableSlots(String username);
}
