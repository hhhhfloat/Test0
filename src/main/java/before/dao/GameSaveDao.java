package before.dao;

import before.controller.GameCtrl;
import modifying.auth.model.Account;
import before.model.entity.MapSaveData;

public interface GameSaveDao {
    // 初始化用的设置用户名
    void setCurrentUser(Account account);

    // 保存地图
    void saveCurrentLoad(MapSaveData mapData, int loadNumber);
    MapSaveData loadSelectedLoad(int loadNumber);


    // 删档
    void delLoadSave(int loadNumber);
    void delSelectedLevelSave(MapSaveData maps, int currentLevel);

    void setGameCtrl(GameCtrl gameCtrl);
}
