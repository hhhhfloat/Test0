package dao.impl;

import com.google.gson.GsonBuilder;
import controller.GameCtrl;
import dao.GameSaveDao;
import com.google.gson.Gson;
import javafx.scene.control.Alert;
import model.entity.Account;
import model.entity.MapSaveData;
import model.entity.SaveDataWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FileGameSaveDao implements GameSaveDao {
    private GameCtrl gameCtrl;
    private Account account;
    private int loadNumber;

    private Path saveRoot;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ---------- Base64 编码/解码 ----------
    private String encodeToBase64(String original) {
        return Base64.getEncoder().encodeToString(original.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeFromBase64(String encoded) {
        byte[] bytes = Base64.getDecoder().decode(encoded);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ---------- 哈希工具 ----------
    private String computeSha256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    // 保存：对原始 JSON 计算哈希，然后 Base64 编码，再包装
    private void saveWithHash(Path filePath, MapSaveData mapData) {
        String originalJson = gson.toJson(mapData);
        String hash = computeSha256(originalJson);
        String encodedJson = encodeToBase64(originalJson);
        SaveDataWrapper wrapper = new SaveDataWrapper(encodedJson, hash);
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(wrapper, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save with hash: " + e.getMessage(), e);
        }
    }

    // 加载：先读取 wrapper，验证哈希（需先解码），若通过则返回解码后的 MapSaveData
    private MapSaveData loadWithHash(Path filePath) {
        if (!Files.exists(filePath)) return null;
        try (FileReader reader = new FileReader(filePath.toFile())) {
            SaveDataWrapper wrapper = gson.fromJson(reader, SaveDataWrapper.class);
            if (wrapper == null || wrapper.getDataJson() == null) {
                // 旧版未混淆格式，尝试迁移
                return loadLegacyAndMigrate(filePath);
            }
            String storedHash = wrapper.getHash();
            String encodedData = wrapper.getDataJson();
            String originalJson;
            try {
                originalJson = decodeFromBase64(encodedData);
            } catch (Exception e) {
                showCorruptedAlertAndDelete(filePath);
                return null;
            }
            String computedHash = computeSha256(originalJson);
            if (!computedHash.equals(storedHash)) {
                showCorruptedAlertAndDelete(filePath);
                return null;
            }
            return gson.fromJson(originalJson, MapSaveData.class);
        } catch (IOException e) {
            showCorruptedAlertAndDelete(filePath);
            return null;
        }
    }

    // 处理旧版明文存档（直接存储 MapSaveData），迁移到新格式（Base64+哈希）
    private MapSaveData loadLegacyAndMigrate(Path filePath) {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            MapSaveData legacyData = gson.fromJson(reader, MapSaveData.class);
            if (legacyData != null) {
                saveWithHash(filePath, legacyData);
                return legacyData;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void showCorruptedAlertAndDelete(Path filePath) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("存档损坏或被篡改");
        alert.setContentText("存档文件验证失败，已自动删除。请重新开始游戏。");
        alert.showAndWait();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {}
    }

    // ---------- 接口实现 ----------
    public FileGameSaveDao(){}

    @Override
    public void setGameCtrl(GameCtrl gameCtrl) {
        this.gameCtrl = gameCtrl;
        this.loadNumber = gameCtrl.getLoadNumber();
    }

    @Override
    public void setCurrentUser(Account account) {
        this.account = account;
        saveRoot = Paths.get("Data","Saves","User_"+account.getSafeUserName());
        try{
            Files.createDirectories(saveRoot);
        } catch (IOException e) {
            throw new RuntimeException("创建用户目录失败：" + saveRoot, e);
        }
    }

    @Override
    public void saveCurrentLoad(MapSaveData mapData, int loadNumber) {
        if (account == null || saveRoot == null || loadNumber == 0) {
            return;
        }
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        saveWithHash(mapPath, mapData);
    }

    @Override
    public MapSaveData loadSelectedLoad(int loadNumber) {
        if (account == null || saveRoot == null || loadNumber == 0) {
            return null;
        }
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        return loadWithHash(mapPath);
    }

    @Override
    public void delLoadSave(int loadNumber){
        if (account == null || saveRoot == null || loadNumber == 0) {
            return;
        }
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        try {
            Files.deleteIfExists(mapPath);
        } catch (Exception ignored) {}
    }

    @Override
    public void delSelectedLevelSave(MapSaveData maps, int currentLevel){
        if(maps != null){
            maps.setMap(currentLevel, new int[0][0]);
            maps.setScore(currentLevel, 0);
            maps.setEliminated(currentLevel,0);
            maps.setBombCount(currentLevel,3);
            maps.setFreezeCount(currentLevel,3);
            maps.setHintCount(currentLevel, 3);
            maps.setRemainTime(currentLevel);
        }
        saveCurrentLoad(maps, loadNumber);
    }
}