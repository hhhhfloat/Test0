package modifying.auth.dao.loadDao;

import before.model.entity.SaveDataWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.dao.LoadDao;
import modifying.shared.model.MapSaveData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

public class FileLoadDao implements LoadDao {

    private static final String BASE_DIR = "Data/Saves";
    private static final String FILE_PREFIX = "LoadSave";
    private static final String FILE_SUFFIX = ".json";

    private LoginCtrl loginCtrl;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileLoadDao(){}
    public FileLoadDao(LoginCtrl loginCtrl){
        this.loginCtrl = loginCtrl;
    }

    // Base 64 serialization
    private String decodeFromBase64(String encoded){
        byte[] bytes = Base64.getDecoder().decode(encoded);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    private String encodeToBase64(String original) {
        return Base64.getEncoder().encodeToString(original.getBytes(StandardCharsets.UTF_8));
    }

    // Hash tools
    private String computeSha256(String content){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    // write the file path
    private Path getSavePath(String safeUserName, int loadNumber) {
        if (loadNumber < 1 || loadNumber > loginCtrl.getTotLoadNumber()) {
            throw new IllegalArgumentException("存档编号必须在 1~5 之间");
        }
        String fileName = FILE_PREFIX + loadNumber + FILE_SUFFIX;
        return Paths.get(BASE_DIR, "User_" + safeUserName, fileName);
    }

    @Override
    public ValidationResult validateSave(String safeUserName, int loadNumber) {
        Path filePath = getSavePath(safeUserName, loadNumber);
        if (!Files.exists(filePath)) {
            return ValidationResult.NOT_FOUND;
        }

        try (FileReader reader = new FileReader(filePath.toFile())) {
            SaveDataWrapper wrapper = gson.fromJson(reader, SaveDataWrapper.class);
            if (wrapper == null || wrapper.getDataJson() == null) {
                deleteSaveFile(filePath);
                return ValidationResult.CORRUPTED;
            }

            // 1. 哈希校验
            String originalJson = decodeFromBase64(wrapper.getDataJson());
            String computedHash = computeSha256(originalJson);
            if (!computedHash.equals(wrapper.getHash())) {
                deleteSaveFile(filePath);
                return ValidationResult.INVALID;
            }

            // 2. 结构验证：尝试反序列化为 MapSaveData
            try {
                MapSaveData mapData = gson.fromJson(originalJson, MapSaveData.class);
                if (mapData == null) {
                    deleteSaveFile(filePath);
                    return ValidationResult.CORRUPTED;
                }
                // 可选：检查关键字段是否非空（例如 map 不能为 null）
                if (mapData.getMap() == null) {
                    deleteSaveFile(filePath);
                    return ValidationResult.CORRUPTED;
                }
            } catch (Exception e) {
                deleteSaveFile(filePath);
                return ValidationResult.CORRUPTED;
            }

            return ValidationResult.VALID;
        } catch (Exception e) {
            deleteSaveFile(filePath);
            return ValidationResult.CORRUPTED;
        }
    }

    @Override
    public boolean deleteSave(String safeUserName, int loadNumber) {
        Path filePath = getSavePath(safeUserName, loadNumber);
        return deleteSaveFile(filePath);
    }

    // ---------- 内部工具 ----------
    private boolean deleteSaveFile(Path filePath) {
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Optional<SaveMetadata> getSaveMetadata(String safeUserName, int loadNumber){
        Path filePath = getSavePath(safeUserName,loadNumber);
        if(!Files.exists(filePath)){
            return Optional.empty();
        }
        try (FileReader reader = new FileReader(filePath.toFile())) {
            SaveDataWrapper wrapper = gson.fromJson(reader, SaveDataWrapper.class);
            if (wrapper == null || wrapper.getDataJson() == null) {
                return Optional.empty();
            }

            String originalJson = decodeFromBase64(wrapper.getDataJson());
            // 如果已经通过 validateSave，这里可以跳过哈希校验，直接解析
            MapSaveData mapData = gson.fromJson(originalJson, MapSaveData.class);
            if (mapData == null) {
                return Optional.empty();
            }

            // 提取数据（假设 MapSaveData 有这些方法）
            int totalSeconds = mapData.getTotalPlayTime(); // 需要你自行添加此方法
            String playTime = formatPlayTime(totalSeconds);
            int score = mapData.getScore();

            return Optional.of(new SaveMetadata(playTime, score));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    // ---------- 辅助方法 ----------
    private String formatPlayTime(int totalSeconds) {
        if (totalSeconds <= 0) return "0";
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        if (hours > 0) {
            return hours + ":" + minutes + ":" + seconds;
        } else {
            return minutes + ":"+seconds;
        }
    }

    @Override
    public void saveSaveData(String safeUserName, int loadNumber, MapSaveData mapSaveData){
        Path filePath = getSavePath(safeUserName, loadNumber);
        // 确保目录存在
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException("无法创建存档目录：" + filePath.getParent(), e);
        }
        String originalJson = gson.toJson(mapSaveData);
        String hash = computeSha256(originalJson);
        String encodedJson = encodeToBase64(originalJson);
        SaveDataWrapper wrapper = new SaveDataWrapper(encodedJson, hash);

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(wrapper, writer);
        } catch (IOException e) {
            throw new RuntimeException("保存存档失败：" + filePath, e);
        }
    }
}
