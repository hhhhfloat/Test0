package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioCtrl {

    private double volume;

    public AudioCtrl(){
        volume = 1.0;
    }
    public AudioCtrl(double volume) {
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    private Media bgMusic = getMedia("bgmusic_Cello");
    private Media buttonSound = getMedia("button");
    private Media clickSound = getMedia("click");
    private Media eliminateSound = getMedia("eliminate");
    private Media bombSound = getMedia("TNT");
    private Media iceBreakSound = getMedia("iceBreak");

    public void playBgMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(bgMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static Media getMedia(String name) {
        Path audioPath;
        Media media;
        audioPath = Paths.get("src","main","resources","Audio",name+".mp3");
        media = new Media(audioPath.toUri().toString());
        return media;
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
        mediaPlayer.setVolume(1);
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
    public void playIceBreakSound(){
        MediaPlayer mediaPlayer = new MediaPlayer(iceBreakSound);
        mediaPlayer.play();
    }

    public void setVolume(double volume) {

    }

    public void toggleMute() {

    }

}