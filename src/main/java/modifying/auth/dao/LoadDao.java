package modifying.auth.dao;

import modifying.shared.model.MapSaveData;

import java.util.Optional;

public interface LoadDao {
    /**
     * 验证指定存档的有效性
     * @param safeUserName 用户安全名（用于构造目录）
     * @param loadNumber 存档编号 (1~5)
     * @return 验证结果
     */
    ValidationResult validateSave(String safeUserName, int loadNumber);

    Optional<SaveMetadata> getSaveMetadata(String safeUserName, int loadNumber);

    /**
     * @param playTime 游玩时间（格式化后的字符串）
     * @param score    当前分数
     */
    record SaveMetadata(String playTime, int score) { }

    void saveSaveData(String safeUserName, int loadNumber, MapSaveData mapSaveData);

    /**
     * 删除指定存档
     * @param safeUserName 用户安全名
     * @param loadNumber 存档编号 (1~5)
     * @return true 删除成功，false 删除失败或文件不存在
     */
    boolean deleteSave(String safeUserName, int loadNumber);

    enum ValidationResult {
        VALID,          // 存档有效，哈希匹配
        INVALID,        // 存档存在但哈希不匹配（已损坏/被篡改）
        NOT_FOUND,      // 文件不存在
        CORRUPTED;      // 文件无法解析（格式错误）
    }
}
