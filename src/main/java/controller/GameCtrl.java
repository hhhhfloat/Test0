package controller;

import dao.GameSaveDao;
import dao.UserDao;
import dao.impl.FileUserDao;
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
import model.entity.LinkyMap;
import view.game_nodes.*;
import view.game_nodes.Interfaces.BoardInterface;
import view.game_nodes.Interfaces.ProgressLabelInterface;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;
import view.game_nodes.Labels.ProgressLabel;
import view.game_nodes.Labels.ScoreLabel;
import view.game_nodes.Labels.TimeLabel;
import view.scenes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class GameCtrl {
    // 用户使用部分
    private Account account;
    private final UserDao userDao;
    private GameSaveDao gameSaveDao;
    // controller 连接部分
    private final SceneCtrl sceneCtrl;
    private final AudioCtrl audioCtrl;
    // 持有view与model引用
    private BoardInterface board;
    private TimeLabelInterface timeLabel;
    private ScoreLabelInterface scoreLabel;
    private ProgressLabelInterface progressLabel;

    private LinkyMap linkyMap;
    private GameScene gameScene;
    // 辅助
    private CellNode selectedCell;
    private int mode;
    private int combo = 0;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl, AudioCtrl audioCtrl, GameSaveDao gameSaveDao) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
        this.audioCtrl = audioCtrl;
        this.gameSaveDao = gameSaveDao;
        selectedCell = null;
    }

    public void setAccount(Account account){
        this.account = account;
        // 将当前User送到SaveDao
        if(account != null) {
            gameSaveDao.setCurrentUser(account.getUserName());
            Properties test = new Properties();
            test.setProperty("volumn","70");
            test.setProperty("free","asdf");
            gameSaveDao.saveConfig(test);
        }
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

    public void handleLoad1() {
        showLevelScene();
    }

    public void handleLoad2() {
        showLevelScene();
    }

    public void handleLoad3() {
        showLevelScene();
    }

    public void handleBack() {
        sceneCtrl.setScene(new AccountScene(account, this));
    }

    public void handleEasy() {
        mode = 0;
        showNewGameScene();
    }

    public void handleDifficult() {
        mode = 1;
        showNewGameScene();
    }

    public void showLoadScene() { sceneCtrl.setScene(new LoadScene(this)); }

    public void handleCellClick(CellNode cellNode) {
        audioCtrl.playClickSound();
        if(linkyMap.getMap()[cellNode.getCrd().x()][cellNode.getCrd().y()]!=-1) {
            if (selectedCell == null) {
                selectedCell = cellNode;
                selectedCell.setHighlight(true);
            } else if (selectedCell == cellNode) {
                cellNode.setHighlight(false);
                selectedCell = null;
            } else {
                ArrayList<Crd> route = linkyMap.pathFindByPoint(selectedCell.getCrd(), cellNode.getCrd());
                if (route.isEmpty()) {
                    selectedCell.setHighlight(false);
                    cellNode.setHighlight(true);
                    selectedCell = cellNode;
                    combo = 0;
                } else {
                    cellNode.setHighlight(true);
                    board.eliminate(selectedCell, cellNode, route);
                    selectedCell = null;
                    linkyMap.delNumMap(route);
                    gameScene.playInformation("Combo " + ++combo + "!\n Score " + (10 + 5 * (combo - 1)));
                    audioCtrl.playEliminateSound();
                    scoreLabel.addScore(combo);
                }
            }
        }
    }

    public void handlePause() {
        sceneCtrl.setScene(new PauseScene(this));
        timeLabel.pauseTime();
    }

    /// 找不到了
    public void handleSave() {

    }

    public void handleExitToMenu() { showLoginScene(); }

    public void handleRestart() {
        //
    }

    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }

    public void timeUp() {

    }

    public void showLoginScene() { sceneCtrl.setScene(new LoginScene(new LoginCtrl(userDao, sceneCtrl, this))); }

    public void showLevelScene() { sceneCtrl.setScene(new LevelScene(this)); }

    private void setLinkyMap(int row, int col, int mode, boolean isPair) {
        linkyMap = new LinkyMap(row, col, mode, isPair);
        board = new Board(row, col, 50, linkyMap, this);
    }

    public void showNewGameScene() {
        if(mode == 0){
            timeLabel = new TimeLabel(180, this);
            setLinkyMap(12, 12, 0, false);
            timeLabel.start();
        } else {
            timeLabel = new TimeLabel(300, this);
            setLinkyMap(12, 12, 1, false);
            timeLabel.start();
        }
        scoreLabel = new ScoreLabel();
        progressLabel = new ProgressLabel();
        gameScene = new GameScene(board, timeLabel, scoreLabel, progressLabel,this);
        sceneCtrl.setScene(gameScene);
    }
}
