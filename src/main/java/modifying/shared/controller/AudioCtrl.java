package modifying.shared.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class AudioCtrl {

    private final double volume;
    private MediaPlayer bgPlayer;

    // 🔥 使用 MediaPlayer 缓存池（每个音效一个实例）
    private final Map<String, MediaPlayer> soundPool = new HashMap<>();

    public AudioCtrl() {
        this(1.0);
    }

    public AudioCtrl(double volume) {
        this.volume = volume;

        // 创建并缓存所有短音效的 MediaPlayer
        createSound("button", "click");
        createSound("click", "click");
        createSound("eliminate", "eliminate");
        createSound("bomb", "TNT");
        createSound("iceBreak", "iceBreak");
        createSound("toggle", "toggle");
    }

    /**
     * 创建并预热一个音效 MediaPlayer
     */
    private void createSound(String key, String fileName) {
        Media media = new Media(getClass().getResource("/Audio/" + fileName + ".mp3").toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setCycleCount(1);

        // 🔥 预热：播放一次并立即暂停，强制解码器加载数据到内存
        player.setOnReady(() -> {
            player.play();
            player.pause();
            player.seek(Duration.ZERO);
        });

        soundPool.put(key, player);
    }

    /**
     * 播放音效（从池中获取并重置）
     */
    private void playSound(String key) {
        MediaPlayer player = soundPool.get(key);
        if (player != null) {
            player.seek(Duration.ZERO);
            player.play();
        }
    }

    // ===== 公开的播放方法 =====
    public void playButtonSound() {
        playSound("button");
    }

    public void playClickSound() {
        playSound("click");
    }

    public void playToggleSound() {
        playSound("toggle");
    }

    public void playEliminateSound() {
        playSound("eliminate");
    }

    public void playBombSound() {
        playSound("bomb");
    }

    public void playIceBreakSound() {
        playSound("iceBreak");
    }

    // ===== 背景音乐 =====
    public void playBgMusic() {
        if (bgPlayer == null) {
            Media media = new Media(getClass().getResource("/Audio/bgmusic_Cello.mp3").toString());
            bgPlayer = new MediaPlayer(media);
            bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgPlayer.setVolume(volume);
        }
        bgPlayer.play();
    }

    public void stopBgMusic() {
        if (bgPlayer != null) bgPlayer.stop();
    }

    public void setBgVolume(double vol) {
        if (bgPlayer != null) bgPlayer.setVolume(vol);
    }

    // ===== 统一音量控制 =====
    public void setVolume(double volume) {
        for (MediaPlayer p : soundPool.values()) {
            p.setVolume(volume);
        }
        if (bgPlayer != null) bgPlayer.setVolume(volume);
    }
}