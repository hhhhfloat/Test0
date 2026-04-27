package dao;

import model.entity.Account;

import java.util.List;

public interface UserDao {
    Account findByName(String username);            // 根据用户名查找账号
    void save(Account account);                     // 保存账号（注册）
    boolean validate(String username, String password);
    List<Account> getAllUsers();                    // 排行榜可能需要
    
}
