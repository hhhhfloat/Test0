package modifying.auth.dao.loadDao;

import before.model.entity.SaveDataWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.dao.LoadDao;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
        if(!Files.exists(filePath)){
            return ValidationResult.NOT_FOUND;
        }
        try(FileReader reader = new FileReader(filePath.toFile())){
            SaveDataWrapper wrapper = gson.fromJson(reader, SaveDataWrapper.class);
            if(wrapper == null || wrapper.getDataJson() == null){
                deleteSaveFile(filePath);
                return ValidationResult.CORRUPTED;
            }
            String storedHash = wrapper.getHash();
            String encodedData = wrapper.getDataJson();

            String originalJson;
            try {
                originalJson = decodeFromBase64(encodedData);
            } catch (Exception e) {
                deleteSaveFile(filePath);
                return ValidationResult.CORRUPTED;
            }

            String computedHash = computeSha256(originalJson);
            if (!computedHash.equals(storedHash)) {
                deleteSaveFile(filePath);
                return ValidationResult.INVALID;
            }

            return ValidationResult.VALID;
        } catch (IOException e){
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
}
