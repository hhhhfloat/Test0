package dao.impl;

import com.google.gson.GsonBuilder;
import dao.GameSaveDao;
import com.google.gson.Gson;
import model.entity.MapSaveData;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class FileGameSaveDao implements GameSaveDao {
    /// user identification
    private String currentUser;
    /// save path
    private Path saveRoot;
    private Path currentUserDir;
    /// add the gson thing
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FileGameSaveDao(){}

    /// SET USER
    @Override
    public void setCurrentUser(String userName) {
        currentUser = userName;
        // set saving path
        saveRoot = Paths.get("Data","Saves","User_"+currentUser);
        try{
            Files.createDirectories(saveRoot);
        }catch(IOException e){
            throw new RuntimeException("创建用户目录失败："+saveRoot,e);
        }
    }


    /// User Config Save & Load
    @Override
    public void saveConfig(Properties config){
        try{// 创建配置文件路径
            Path configPath = saveRoot.resolve("ConfigSave.properties");
            // 输出
            try (OutputStream out = Files.newOutputStream(configPath)) {;
                config.store(out, "Game Config for " + currentUser);
            }
        }catch(Exception e) {
            throw new RuntimeException("保存配置文件失败："+e.getMessage(),e);
        }
    }
    @Override
    public Properties loadConfig(){
        Properties config = new Properties();
        if(currentUser == null || saveRoot == null){
            return config;
        }
        Path configPath = saveRoot.resolve("ConfigSave.properties");
        if(Files.exists(configPath)){
            try(InputStream in = Files.newInputStream(configPath)){
                config.load(in);
            }catch(IOException e){
                throw new RuntimeException("加载配置文件失败: " + e.getMessage(), e);
            }
        }
        return config;
    }


    @Override
    public void saveMap(MapSaveData mapData){
        if(currentUser == null || saveRoot == null){
            throw new IllegalStateException("Undefined user");
        }
        Path mapPath = saveRoot.resolve("MapSave.json");
        try(FileWriter writer = new FileWriter(mapPath.toFile())){
            gson.toJson(mapData,writer);
        }catch(IOException e){
            throw new RuntimeException("Failed map data saving: "+e.getMessage(),e);
        }
    }
    @Override
    public MapSaveData loadMaps() {
        return new MapSaveData();
    }


}
