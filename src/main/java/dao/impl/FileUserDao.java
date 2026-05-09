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
        userFile  = Paths.get("Data/users.properties");
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(userFile)) {
            props.load(in);
        } catch (IOException e) {
            System.err.println("读取用户文件失败: " + e.getMessage());
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
    public boolean exist(String username) {
        Properties properties = loadProperties();
        return properties.contains(username);
    }

    @Override
    public boolean validate(String username, String password) {
        Properties properties = loadProperties();
        String storedPwd = properties.getProperty(username + ".pwd");
        return storedPwd != null && storedPwd.equals(password);
    }

    @Override
    public void createUser(String username, String password) {
        Properties properties = loadProperties();
        properties.setProperty(username + ".pwd", password);
        properties.setProperty(username + ".highscore", "0");
        saveProperties(properties);
    }

    @Override
    public int getHighScore(String username) {
        Properties properties = loadProperties();
        String scoreStr = properties.getProperty(username + ".highscore");
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