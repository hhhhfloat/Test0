package controller;

import dao.UserDao;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Account;
import model.entity.Crd;
import view.labels.TimerLabel;
import view.scenes.*;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

public class GameCtrl {
    private final UserDao userDao;
    private final SceneCtrl sceneCtrl;
    private Account account;
    private GameScene gameScene;
    private Timeline gameTimeline;
    private int remainingTime = 300;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
    }

    /// 时间控制
    private void startGameTimer()
    {
        if(gameTimeline != null)
            gameTimeline.stop();
        // 创建新的Timeline
        gameTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1),e->{
                    remainingTime--;
                    if(gameScene != null){
                        gameScene.updateTime(remainingTime);
                    }
                    if(remainingTime<=0){
                        gameTimeline.stop();
                        // 添加游戏结束逻辑

                    }
                })
        );
        gameTimeline.setCycleCount(Timeline.INDEFINITE); // 无限循环
        gameTimeline.play(); // 开始计时
    }

    /// 登录控制（为什么不在LoginCtrl）
    public void setAccount(Account account) {
        this.account = account;
    }

    public void handleStart() { showLoadScene(); }

    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait();
        showLoginScene();
    }

    public void handleLeaderboard() {
        VBox list = new VBox(10);
        for (int i = 1; i <= 30; i++) {
            Label menuItem = new Label("No." + i);
            menuItem.setMaxWidth(Double.MAX_VALUE);
            menuItem.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            list.getChildren().add(menuItem);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(300, 300);

        Scene scene = new Scene(scrollPane, 350, 500);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setScene(scene);
        leaderboardStage.show();
    }

    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("All the unsaved data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    /// 读档控制
    public void handleLoad1() {
        showLevelScene();
    }

    public void handleLoad2() {

    }

    public void handleLoad3() {

    }

    public void handleBack() {
        sceneCtrl.setScene(new AccountScene(account, this));
    }

    public void handleEasy() {
        showGameScene(0);
    }

    public void handleDifficult() {
        showGameScene(1);
    }

    public void showLoadScene() {
        sceneCtrl.setScene(new LoadScene(this));
    }

    public void handleCellClick(Crd p) {

    }

    public void handlePause() {

    }

    public void handleSave() {

    }

    public void handleExitToMenu() {

    }

    public void handleRestart() {

    }

    public int getCurrentScore() {
        int score = 0;
        return score;
    }

    public int getRemainTime() {
        int time = 0;
        return time;
    }

    public void showLoginScene() { sceneCtrl.setScene(new LoginScene(new LoginCtrl(userDao, sceneCtrl, this))); }

    public void showLevelScene() {
        sceneCtrl.setScene(new LevelScene(this));
    }

    public void showGameScene(int mode) {
        gameScene = new GameScene(this);
        sceneCtrl.setScene(new GameScene(this));
    }

    // 供view刷新用
    public boolean isGameRunning() {
        return true;
    }


    public void handleLoad() {

    }

    public void handleContinue() {
        // 关闭暂停窗口，回到游戏场景
        sceneCtrl.setScene(gameScene);
        if(gameTimeline != null) gameTimeline.play();
    }
}
