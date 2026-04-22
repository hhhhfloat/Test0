package LoginPage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Accounts {
    String account;

    public static Map<String,String> read() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))){
            String line = reader.readLine();
            while (line.indexOf('&')!=-1) {
                users.put(line.split("&")[0],line.split("&")[1]);
            }
        } catch (IOException e) {
        }
        return users;
    }
}

