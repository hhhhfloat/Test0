package LoginPage;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Accounts {
    public static Map<String,String> read() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/userdata/users.txt"))){
            String line = reader.readLine();

            while (line!=null&&line.indexOf('&')!=-1) {
                users.put(line.split("&")[0],line.split("&")[1]);
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        return users;
    }
}

