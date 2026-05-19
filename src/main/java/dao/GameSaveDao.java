package dao;

import java.util.Properties;
import model.entity.MapSaveData;

import java.io.IOException;

public interface GameSaveDao {
    // 初始化用的设置用户名
    void setCurrentUser(String userName);

    // 保存地图
    void saveMap(MapSaveData mapData,int loadNumber);
    MapSaveData loadMaps(int loadNumber);

    // 保存Config
    Properties loadConfig();
    void saveConfig(Properties config);

    // 删档
    void delSave(int loadNumber, int mode);
}
