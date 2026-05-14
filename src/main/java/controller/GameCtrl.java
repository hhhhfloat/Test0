package controller;

import dao.GameSaveDao;
import dao.UserDao;
import javafx.application.Platform;
import javafx.scene.Parent;
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
import model.entity.MapSaveData;
import view.game_nodes.*;
import view.game_nodes.Interfaces.BoardInterface;
import view.game_nodes.Interfaces.ProgressLabelInterface;
import view.game_nodes.Interfaces.ScoreLabelInterface;
import view.game_nodes.Interfaces.TimeLabelInterface;
import view.game_nodes.Labels.ProgressLabel;
import view.game_nodes.Labels.ScoreLabel;
import view.game_nodes.Labels.TimeLabel;
import view.scenes.*;

import java.util.ArrayList;
import java.util.Properties;

public class GameCtrl extends Parent {
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
    private int loadNumber = 0;
    private boolean bombMode = false;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl, AudioCtrl audioCtrl, GameSaveDao gameSaveDao) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
        this.audioCtrl = audioCtrl;
        this.gameSaveDao = gameSaveDao;
        selectedCell = null;
    }

    public void setAccount(Account account) {
        this.account = account;
        // 将当前User送到SaveDao
        if (account != null) {
            gameSaveDao.setCurrentUser(account.getUserName());
        }
    }


    /// 按钮功能

    /// 登录后界面
    public void handleStart() {
        if(account!=null)
        {
            showLoadScene();
        }
        else{
            loadNumber = 0;
            handleLoad0();
        }
    }
    public void handleLogout() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait();
        account = null;
        loadNumber = 0;
        showLoginScene();
    }

    public void handleLeaderboard() {
        audioCtrl.playButtonSound();
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
        audioCtrl.playButtonSound();
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
    /// 读档界面（游客跳过）
    public void handleLoad0(){
        loadNumber = 0;
        showLevelScene();
    }
    public void handleLoad1() {
        audioCtrl.playButtonSound();
        loadNumber = 1;
        showLevelScene();
    }

    public void handleLoad2() {
        audioCtrl.playButtonSound();
        loadNumber = 2;
        showLevelScene();
    }

    public void handleLoad3() {
        audioCtrl.playButtonSound();
        loadNumber = 3;
        showLevelScene();
    }

    public void handleBack() {
        audioCtrl.playButtonSound();
        sceneCtrl.setScene(new AccountScene(account, this));
    }

    public void handleEasy() {
        audioCtrl.playButtonSound();
        mode = 0;
        showNewGameScene();
    }

    public void handleDifficult() {
        audioCtrl.playButtonSound();
        mode = 1;
        showNewGameScene();
    }
    public void handlePause() {
        sceneCtrl.setScene(new PauseScene(this));
        timeLabel.pauseTime();
    }
    /// 游戏内暂停按钮功能
    public void handleSave() {
        // 游客模式特殊处理
        if(loadNumber == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminding");
            alert.setHeaderText("You can't save in visitor mode!");
            alert.showAndWait();
            return;
        }
        // save map
        MapSaveData data = new MapSaveData(loadNumber);
        data.setMap(mode,linkyMap.getMap());
        data.setScore(mode, scoreLabel.getScore());
        data.setRemainTime(mode,timeLabel.getRemainingTime());
        gameSaveDao.saveMap(data,loadNumber);
        // 设置config
        Properties config = new Properties();
        config.setProperty("volumn",String.valueOf(audioCtrl.getVolume()));
        gameSaveDao.saveConfig(config);

        System.out.println("Game Saved!");
    }
    public void handleExitToMenu() {
        account = null;
        loadNumber = 0;
        showLoginScene();
    }
    public void handleRestart() {
        //
    }
    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }

    /// Scene转换
    public void showLoadScene() { sceneCtrl.setScene(new LoadScene(this)); }
    public void showLoginScene() { sceneCtrl.setScene(new LoginScene(new LoginCtrl(userDao, sceneCtrl, this))); }
    public void showLevelScene() { sceneCtrl.setScene(new LevelScene(this)); }
    public void showNewGameScene() {
        int row = 12,col = 12;
        boolean isPair = false;
        MapSaveData maps = gameSaveDao.loadMaps(loadNumber);
        Properties config = gameSaveDao.loadConfig();
        // load or reset info
        if(maps != null && config != null)
        {
            setLinkyMap(maps);
            System.out.println("map loaded");
            timeLabel = new TimeLabel(maps.getRemainTime(mode), this);
            audioCtrl.setVolume(50.0);
            timeLabel.start();
            scoreLabel = new ScoreLabel(maps.getScore(mode));
        }
        else{
            linkyMap = new LinkyMap(row, col, mode, isPair);
            System.out.println("Default map applied");
            audioCtrl.setVolume(50.0);
            timeLabel = new TimeLabel((mode == 0) ? 180 : 300, this);
            scoreLabel = new ScoreLabel();
            timeLabel.start();
        }
        board = new Board(row, col, 50, linkyMap, this);
        progressLabel = new ProgressLabel();
        gameScene = new GameScene(board, timeLabel, scoreLabel, progressLabel,this);
        sceneCtrl.setScene(gameScene);
    }

    public void handleCellClick(CellNode cellNode) {
        audioCtrl.playClickSound();
        if (linkyMap.getMap()[cellNode.getCrd().x()][cellNode.getCrd().y()] != -1) {
            if (selectedCell == null) {
                selectedCell = cellNode;
                selectedCell.setHighlight(true);
                if(bombMode) { selectedCell.setBomb(true); }
            } else if (selectedCell == cellNode) {
                cellNode.setHighlight(false);
                selectedCell.setBomb(false);
                selectedCell = null;
            } else {
                ArrayList<Crd> route = linkyMap.pathFindByPoint(selectedCell.getCrd(), cellNode.getCrd());
                if (route.isEmpty()) {
                    selectedCell.setHighlight(false);
                    cellNode.setHighlight(true);
                    selectedCell = cellNode;
                    combo = 0;
                } else if (bombMode && cellNode.getType() != selectedCell.getType()){
                    selectedCell.setBomb(false);
                    selectedCell.setHighlight(false);
                    cellNode.setBomb(true);
                    selectedCell = cellNode;
                } else {
                    eliminate(selectedCell, cellNode, route);
                }
            }
        }
    }

    public void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList route) {
        cellNode1.setHighlight(true);
        cellNode2.setHighlight(true);
        if(bombMode) {
            cellNode1.setBomb(true);
            cellNode2.setBomb(true);
            cellNode1.eliminateCell();
            cellNode2.eliminateCell();
            audioCtrl.playBombSound();
            bombMode = false;
        }
        else {
            board.eliminate(cellNode1, cellNode2, route);
            audioCtrl.playEliminateSound();
        }
        selectedCell = null;
        linkyMap.delNumMap(route);
        gameScene.playInformation("Combo " + ++combo + "!\n Score " + (10 + 5 * (combo - 1)));
        scoreLabel.addScore(combo);
        if(linkyMap.isComplete()){
            showWinScene();
        }
    }

    public void handleBombMode() {
        audioCtrl.playButtonSound();
        if(selectedCell != null) {
            selectedCell.setHighlight(false);
            selectedCell = null;
        }
        bombMode = true;
    }

    //Pause Control
    public void handlePause() {
        sceneCtrl.setScene(new PauseScene(this));
        timeLabel.pauseTime();
    }

    public void handleSave() {
        MapSaveData data = new MapSaveData(loadNumber);
        if (linkyMap.getMapType() == 1) {
            data.setHardMap(linkyMap.getMap());
            data.setIsPair(linkyMap.getIsPair() == 2);
        } else {
            data.setIsPair(linkyMap.getIsPair() == 2);
            data.setEasyMap(linkyMap.copyMap());
        }
        gameSaveDao.saveMap(data, loadNumber);

        Properties config = new Properties();
        config.setProperty("volumn", String.valueOf(123));
        gameSaveDao.saveConfig(config);
        System.out.println("Game Saved!");
    }

    public void timeUp() {
        sceneCtrl.setScene(new GameoverScene(this));
    }
    public void showWinScene(){
        sceneCtrl.setScene(new WinScene(this));
    }


    private void setLinkyMap(MapSaveData maps) {
        int row = 12,col = 12;
        boolean isPair = false;
        if(maps.getMap(mode).length==row&&maps.getMap(mode)[0].length==col){
            System.out.println("Map save loaded");
            linkyMap = new LinkyMap(row, col, maps.getMap(mode));
        }
        else {
            System.out.println("Default map applied for this mode");
            linkyMap = new LinkyMap(row, col, mode, isPair);
        }
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
