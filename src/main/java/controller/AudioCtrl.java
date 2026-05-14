package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class AudioCtrl {

    private double volume;

    public AudioCtrl(){
        volume = 50.0;
    }
    public AudioCtrl(double volumn) {
        this.volume = volumn;
    }

    public double getVolume() {
        return volume;
    }

    private Media bgMusic = getMedia("bgMusic");
    private Media buttonSound = getMedia("button");
    private Media clickSound = getMedia("click");
    private Media eliminateSound = getMedia("eliminate");
    private Media bombSound = getMedia("TNT");

    public void playBgMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static Media getMedia(String name) {
        return new Media(Paths.get("src/main/resources/Audio/" + name + ".mp3").toUri().toString());
    }

    public void pauseBgMusic() {

    }

    public void resumeBgMusic() {

    }

    public void playButtonSound() {
        MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
        mediaPlayer.play();
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

    public void playBombSound() {
        MediaPlayer mediaPlayer = new MediaPlayer(bombSound);
        mediaPlayer.play();
    }

    public void setVolume(double volume) {

    }

    public void toggleMute() {

    }

}
