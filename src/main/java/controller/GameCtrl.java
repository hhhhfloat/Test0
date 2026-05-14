package controller;

import dao.GameSaveDao;
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
    private int loadNumber;
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

    private void setLinkyMap(int row, int col, int mode, boolean isPair) {
        MapSaveData maps = gameSaveDao.loadMaps(loadNumber);
        if (maps == null) {
            linkyMap = new LinkyMap(row, col, mode, isPair);
            System.out.println("Default map applied");
        } else if (mode == 1 && maps.getHardMap() != null) {
            System.out.println("Map save loaded");
            linkyMap = new LinkyMap(row, col, maps.getHardMap());
        } else if (mode == 0 && maps.getEasyMap() != null) {
            linkyMap = new LinkyMap(row, col, maps.getEasyMap());
            System.out.println("Map save loaded");
        } else {
            System.out.println("Default map applied for this mode");
            linkyMap = new LinkyMap(row, col, mode, isPair);
        }
        board = new Board(row, col, 50, linkyMap, this);
    }

    //Account Scene Control
    public void handleStart() {
        audioCtrl.playButtonSound();
        showLoadScene();
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait();
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

    //Load Scene Control
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

    //Level Scene Control
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

    //Board Control
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
                if (!bombMode && route.isEmpty()) {
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

    public void handleExitToMenu() {
        showAccountScene();
    }

    public void handleRestart() {
        //
    }

    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }

    //
    public void timeUp() {

    }

    //Scene Control
    public void showLoginScene() {
        sceneCtrl.setScene(new LoginScene(new LoginCtrl(userDao, audioCtrl, sceneCtrl, this)));
    }

    public void showLevelScene() {
        sceneCtrl.setScene(new LevelScene(this));
    }

    public void showLoadScene() {
        sceneCtrl.setScene(new LoadScene(this));
    }

    public void showAccountScene() {
        sceneCtrl.setScene(new AccountScene(this));
    }

    public void showNewGameScene() {
        if (mode == 0) {
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
        gameScene = new GameScene(board, timeLabel, scoreLabel, progressLabel, this);
        sceneCtrl.setScene(gameScene);
    }
}
