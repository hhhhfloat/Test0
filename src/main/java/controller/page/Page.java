package controller.page;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import view.scene.InitialScene;

import java.nio.file.Paths;

public class Page extends Application {
    void playMusic() {
        Media media = new Media(Paths.get("src/main/resources/bgmusic.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void start(Stage stage) {
        playMusic();
        Scene primaryScene = InitialScene.initialScene();
        stage.setScene(primaryScene);
        stage.show();
    }
}
