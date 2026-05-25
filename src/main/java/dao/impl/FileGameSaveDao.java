package dao.impl;

import com.google.gson.GsonBuilder;
import controller.GameCtrl;
import dao.GameSaveDao;
import com.google.gson.Gson;
import model.entity.Account;
import model.entity.MapSaveData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;


public class FileGameSaveDao implements GameSaveDao {
    private GameCtrl gameCtrl;
    private Account account;
    private int loadNumber;
    /// user identification
    /// save path
    private Path saveRoot;
    private Path currentUserDir;
    /// add the gson thing
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileGameSaveDao(){};

    @Override
    public void setGameCtrl(GameCtrl gameCtrl) {
        this.gameCtrl = gameCtrl;
        this.loadNumber = gameCtrl.getLoadNumber();
    }

    /// SET USER
    @Override
    public void setCurrentUser(Account account) {
        this.account = account;
        // set saving path
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
        try (FileWriter writer = new FileWriter(mapPath.toFile())) {
            gson.toJson(mapData, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed map data saving: " + e.getMessage(), e);
        }
    }

    @Override
    public MapSaveData loadSelectedLoad(int loadNumber) {
        if (account == null || saveRoot == null || loadNumber == 0) {
            return null;
        }
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        if (!Files.exists(mapPath)) {
            return null;
        }
        try (FileReader reader = new FileReader(mapPath.toFile())) {
            return gson.fromJson(reader, MapSaveData.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed map loading: " + e.getMessage(), e);
        }
    }

    @Override
    public void delLoadSave(int loadNumber){
        // map delete
        if (account== null || saveRoot == null || loadNumber == 0) {
            return;
        }
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        try {
            if (Files.exists(mapPath)) {
                Files.delete(mapPath);
            }
        } catch (Exception e) {return;}
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
        Path mapPath = saveRoot.resolve("LoadSave" + loadNumber + ".json");
        try (FileWriter writer = new FileWriter(mapPath.toFile())) {
            gson.toJson(maps, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed map data saving: " + e.getMessage(), e);
        }

    }
}
