package dao.impl;

import dao.UserDao;
import model.entity.Account;
import model.state.ScoreEntry;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUserDao implements UserDao {
    private final Path userFile;
    public FileUserDao() {
        Path dataDir = Paths.get("./data");
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            System.err.println("无法创建数据目录: " + e.getMessage());
        }
        this.userFile = dataDir.resolve("users.properties");
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        if (!Files.exists(userFile)) {
            try {
                Files.createFile(userFile);
            } catch (IOException e) {
                System.err.println("无法创建用户文件: " + e.getMessage());//err流？
            }
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        if (Files.exists(userFile)) {
            try (InputStream in = Files.newInputStream(userFile)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("读取用户文件失败: " + e.getMessage());
            }
        }
        return props;
    }

    private void saveProperties(Properties props) {
        try (OutputStream out = Files.newOutputStream(userFile)) {
            props.store(out, "User Accounts");
        } catch (IOException e) {
            System.err.println("保存用户文件失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validate(String username, String password) {
        Properties props = loadProperties();
        String storedPwd = props.getProperty(username + ".pwd");
        return storedPwd != null && storedPwd.equals(password);
    }

    @Override
    public boolean createUser(String username, String password) {
        Properties props = loadProperties();
        if (props.containsKey(username + ".pwd")) {
            return false;
        }
        props.setProperty(username + ".pwd", password);
        props.setProperty(username + ".highscore", "0");
        saveProperties(props);
        return true;
    }

    @Override
    public int getHighScore(String username) {
        Properties props = loadProperties();
        String scoreStr = props.getProperty(username + ".highscore");
        if (scoreStr == null) return -1;
        try {
            return Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void updateHighScore(String username, int score) {
        int currentHigh = getHighScore(username);
        if (score > currentHigh) {
            Properties props = loadProperties();
            props.setProperty(username + ".highscore", String.valueOf(score));
            saveProperties(props);
        }
    }

    @Override
    public List<ScoreEntry> getLeaderboard(int limit) {
        Properties props = loadProperties();
        Set<String> usernames = new HashSet<>();
        for (String key : props.stringPropertyNames()) {
            if (key.endsWith(".pwd")) {
                String username = key.substring(0, key.length() - 4);
                usernames.add(username);
            }
        }

        List<ScoreEntry> entries = new ArrayList<>();
        for (String username : usernames) {
            int score = getHighScore(username);
            entries.add(new ScoreEntry(username, score));
        }

        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        if (entries.size() > limit) {
            entries = entries.subList(0, limit);
        }
        return entries;
    }

    @Override
    public Account findByUsername(String username) {
        Properties props = loadProperties();
        String pwd = props.getProperty(username + ".pwd");
        if (pwd == null) return null;

        Account account = new Account(username);
        account.setPassword(pwd);
        return account;
    }
}