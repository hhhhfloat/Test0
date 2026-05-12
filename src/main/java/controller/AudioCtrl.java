package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class AudioCtrl {
    private Media bgMusic = new Media(getPath("src/main/resources/Audio/bgmusic.mp3"));
    private Media clickSound = new Media(getPath("src/main/resources/Audio/click.mp3"));
    private Media eliminateSound = new Media(getPath("src/main/resources/Audio/eliminate.mp3"));

    public AudioCtrl() {

    }
    private int volume;
    public void playBgMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static String getPath (String path) {
        return Paths.get(path).toUri().toString();
    }

    public void pauseBgMusic() {

    }

    public void resumeBgMusic() {

    }

    public void playClickSound() {
        MediaPlayer mediaPlayer = new MediaPlayer(clickSound);
        mediaPlayer.setVolume(100);
        mediaPlayer.play();
    }

    public void playEliminateSound() {
        MediaPlayer mediaPlayer = new MediaPlayer(eliminateSound);
        mediaPlayer.play();
    }

    public void setVolume(double volume) {

    }

    public void toggleMute() {

    }

}
