package controller;

import LoginPage.LoginScene;
import javafx.scene.Scene;
import view.scene.*;

public class SceneCtrl {
    static Scene primaryScene;

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void switchToInitial() {
        primaryScene = InitialScene.initialScene();
    }

    public static void switchToLogin() {
        primaryScene = LoginScene.loginScene();
    }

    public static void switchToRegister() {
        primaryScene = InitialScene.initialScene();
    }

    public static void switchToAccount() {
        primaryScene = AccountScene.accountScene();
    }

    public static void switchToGame() {
        primaryScene = GameScene.gameScene();
    }

    public static void switchToLevelSelect() {
        primaryScene = LevelScene.levelScene();
    }

}
