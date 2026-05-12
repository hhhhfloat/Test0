package dao;

import java.util.Properties;
import model.entity.MapSaveData;

import java.io.IOException;

public interface GameSaveDao {
    // 初始化用的设置用户名
    void setCurrentUser(String userName);

    // 保存地图
    void saveMap(MapSaveData mapData)throws IOException;
    MapSaveData loadMaps() throws IOException;
    void saveConfig(Properties config);
}
