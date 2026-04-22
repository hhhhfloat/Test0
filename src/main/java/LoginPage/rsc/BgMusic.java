package LoginPage.rsc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BgMusic {
    public static void play() {
        Media media = new Media("file:./src/main/resources/bgmusic.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
}
