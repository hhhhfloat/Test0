package app;

import controller.GameCtrl;
import controller.LoginCtrl;
import controller.SceneCtrl;
import dao.UserDao;
import dao.impl.FileUserDao;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class MainApp extends Application {
    void playMusic() {
        Media media = new Media(Paths.get("src/main/resources/Audio/bgmusic.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void start(Stage stage) {
        playMusic();
        UserDao userDao = new FileUserDao();//接口实例化什么意思？
        SceneCtrl sceneCtrl = new SceneCtrl(stage);
        GameCtrl gameCtrl = new GameCtrl(userDao, sceneCtrl);
        LoginCtrl loginCtrl = new LoginCtrl(userDao, sceneCtrl);
        loginCtrl.showLoginScene();
    }
}
