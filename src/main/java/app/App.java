package app;

import controller.SceneCtrl;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class App extends Application {
    void playMusic() {
        Media media = new Media(Paths.get("src/main/resources/bgmusic.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void start(Stage stage) {
        playMusic();
        stage.setScene(SceneCtrl.getPrimaryScene());
        stage.show();
    }
}
