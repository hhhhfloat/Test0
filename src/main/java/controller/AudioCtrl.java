package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class AudioCtrl {
    private Media bgMusic;
    public AudioCtrl(String audioPath) {
        bgMusic = new Media(Paths.get(audioPath).toUri().toString());
    }
    public void playBgMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void stopBgMusic() {

    }

    public void pauseBgMusic() {

    }

    public void resumeBgMusic() {

    }

    public void playSoundEffect(String effectName) {

    }

    public void setVolume(double volume) {

    }

    public void toggleMute() {

    }

}
