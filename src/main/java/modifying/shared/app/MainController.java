package modifying.shared.app;

import javafx.stage.Stage;
import modifying.auth.launcher.AuthModule;

public class MainController {

    public static void main(String[] args){
        runAuthModule();
    }

    public static void runAuthModule(){
        AuthModule authModule = new AuthModule();
        Stage authStage = new Stage();
        authModule.start(authStage);
    }

}
