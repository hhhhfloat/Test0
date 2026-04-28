package controller;

import LoginPage.Account;
import LoginPage.LoginScene_;
import javafx.scene.Scene;
import view.scene.*;

public class SceneCtrl {
    static Scene primaryScene;

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void switchToLogin() {
        primaryScene = LoginScene.loginScene();
    }

    public static void switchToRegister() {
        primaryScene = InitialScene.initialScene();
    }

    public static void switchToAccount(Account acc, boolean tourist) {
        primaryScene = AccountScene.accountScene();
    }

    public static void switchToGame() {
        primaryScene = GameScene.gameScene();
    }

    public static void switchToLevelSelect() {
        primaryScene = LevelScene.levelScene();
    }

}
