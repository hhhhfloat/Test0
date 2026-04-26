package LoginPage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Account {
    private final Map<String, String> users = new HashMap<>();
    private String accountName;
    public Map<String, String> getMap(){
        return users;
    }
    public Account() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/userdata/users.txt"))) {
            String line = reader.readLine();
            while (line != null && line.indexOf('&') != -1) {
                users.put(line.split("&")[0], line.split("&")[1]);
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
    }
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

