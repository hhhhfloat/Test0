package dao.impl;

import dao.UserDao;
import model.entity.Account;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUserDao implements UserDao {
    private final Path userDataPath;

    @Override
    public List<Account> getAllUsers() {
        List<Account> users = new ArrayList<>();
        Properties props = loadProperties();
        // 假设 properties 中的 key 格式为 "username.pwd"，提取所有 username
        Set<String> keys = props.stringPropertyNames();
        Set<String> processed = new HashSet<>();
        for (String key : keys) {
            if (key.endsWith(".pwd")) {
                String username = key.substring(0, key.length() - 4);
                if (!processed.contains(username)) {
                    //Account acc = new Account(username);
                    //acc.setPassword(props.getProperty(key));
                    //users.add(acc);
                    processed.add(username);
                }
            }
        }
        return users;
    }

    // 构造函数中确定文件位置
    public FileUserDao() {
        // 存在用户主目录下的 .game/data/users.prop
        String home = System.getProperty("user.home");
        this.userDataPath = Paths.get(home, ".game", "data", "users.prop");
    }

    @Override
    public Account findByName(String username) {
        Properties props = loadProperties();
        String password = props.getProperty(username + ".pwd");
        if (password == null) return null;
        Account acc = new Account(username);
        acc.setPassword(password);
         //可能还读取其他属性
        return acc;
    }

    @Override
    public void save(Account account) {
        Properties props = loadProperties();
        props.setProperty(account.getAccountName() + ".pwd", account.getPassword());
        saveProperties(props);
    }

    @Override
    public boolean validate(String username, String password) {
        Account acc = findByName(username);
        return acc != null && acc.getPassword().equals(password);
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(userDataPath)) {
            props.load(in);
        } catch (IOException e) {
            // 文件不存在就返回空 Properties
        }
        return props;
    }

    private void saveProperties(Properties props) {
        try (OutputStream out = Files.newOutputStream(userDataPath)) {
            props.store(out, "User Accounts");
        } catch (IOException e) {
            throw new RuntimeException("保存用户数据失败", e);
        }
    }
}