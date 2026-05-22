package controller;

import dao.GameSaveDao;
import dao.UserDao;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import model.entity.Account;
import model.entity.Crd;
import model.entity.LinkyMap;
import model.entity.MapSaveData;
import view.InformationUtil;
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
    private final GameSaveDao gameSaveDao;
    // controller 连接部分
    private final SceneCtrl sceneCtrl;
    private final AudioCtrl audioCtrl;
    private LoginCtrl loginCtrl;
    // 持有view与model引用
    private BoardInterface board;
    private TimeLabelInterface timeLabel;
    private ScoreLabelInterface scoreLabel;
    private ProgressLabelInterface progressLabel;

    private LinkyMap linkyMap;

    private final LevelScene levelScene;
    private GameScene gameScene;
    // 辅助
    private CellNode selectedCell;
    private int currentLevel;
    private int combo = 0;
    private int loadNumber = 0;
    private boolean bombMode = false;
    private ArrayList<Crd> hintPath;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl, AudioCtrl audioCtrl, GameSaveDao gameSaveDao) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
        this.audioCtrl = audioCtrl;
        this.gameSaveDao = gameSaveDao;
        levelScene = new LevelScene(this);
        selectedCell = null;
    }

    public BoardInterface getBoard() {
        return board;
    }
    public GameSaveDao getGameSaveDao() {
        return gameSaveDao;
    }
    public ScoreLabelInterface getScoreLabel() {
        return scoreLabel;
    }
    public TimeLabelInterface getTimeLabel() {
        return timeLabel;
    }
    public ProgressLabelInterface getProgressLabel() {
        return progressLabel;
    }

    public void setAccount(Account account) {
        this.account = account;
        // 将当前User送到SaveDao
        if (account != null) {
            gameSaveDao.setCurrentUser(account.getUserName());
        }
    }

    public void setLoadNumber(int num) {
        loadNumber = num;
    }

    public void setLoginCtrl(LoginCtrl loginCtrl) {
        this.loginCtrl = loginCtrl;
    }

    private void setLinkyMap(MapSaveData maps) {
        int row = 12, col = 12;
        boolean isPair = false;
        if (maps.getMap(currentLevel).length == row && maps.getMap(currentLevel)[0].length == col) {
            System.out.println("Map save loaded");
            linkyMap = new LinkyMap(row, col, maps.getMap(currentLevel));
        } else {
            System.out.println("Default map applied for this mode");
            linkyMap = new LinkyMap(row, col, currentLevel, isPair);
        }
    }

    //Account Scene
    public void handleStart() {
        audioCtrl.playButtonSound();
        if (account != null) {
            showLoadScene();
        } else {
            loadNumber = 0;
            handleLoad0();
        }
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                account = null;
                loadNumber = 0;
                showLoginScene();
            }
        });
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
        alert.setContentText("Game will be automatically saved.");
        if (loadNumber != 0) {
            handleSave(false);
        }
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    //Load Scene
    public void handleLoad0() {
        loadNumber = 0;
        showLevelScene();
    }

    public void handleSave(boolean isShowed) {
        if (loadNumber == 0 && isShowed) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminding");
            alert.setHeaderText("You can't save in visitor mode!");
            alert.showAndWait();
            return;
        }
        // save map
        MapSaveData data = new MapSaveData(loadNumber);
        data.setMap(currentLevel, linkyMap.getMap());
        data.setScore(currentLevel, scoreLabel.getScore());
        data.setRemainTime(currentLevel, timeLabel.getRemainingTime());
        gameSaveDao.saveMap(data, loadNumber);
        // 设置config
        Properties config = new Properties();
        config.setProperty("volume", String.valueOf(audioCtrl.getVolume()));
        gameSaveDao.saveConfig(config);

        if(isShowed){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminding");
            alert.setHeaderText("Game saved!");
            alert.showAndWait();
        }
    }

    public void deleteLoad(int loadNumber) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to delete this load?");
        alert.setContentText("All the data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gameSaveDao.delMapSave(loadNumber, 0);
                gameSaveDao.delMapSave(loadNumber, 1);
            }
        });
    }

    public void handleLoad1() {
        audioCtrl.playButtonSound();
        loadNumber = 1;
        showLevelScene();
    }

    public void handleDelete1() {
        deleteLoad(1);
    }

    public void handleLoad2() {
        audioCtrl.playButtonSound();
        loadNumber = 2;
        showLevelScene();
    }

    public void handleDelete2() {
        deleteLoad(2);
    }

    public void handleLoad3() {
        audioCtrl.playButtonSound();
        loadNumber = 3;
        showLevelScene();
    }

    public void handleDelete3() {
        deleteLoad(3);
    }

    public void handleBack() {
        audioCtrl.playButtonSound();
        loginCtrl.showAccountScene();
    }

    //Level Scene
    public void handleLevel(int ind) {
        audioCtrl.playButtonSound();
        currentLevel = --ind;
        showNewGameScene();
    }


    //Game Scene
    public void handleBombMode() {
        audioCtrl.playButtonSound();
        if(bombMode){
            if(selectedCell!=null){
                selectedCell.setBomb(false);
                selectedCell.setHighlight(false);
                selectedCell = null;
            }
            bombMode = false;
            GameScene.bombLightOff();
        }
        else{
            if(bombCount>0){
                if(selectedCell != null){
                    selectedCell.setBomb(false);
                    selectedCell.setHighlight(false);
                    selectedCell = null;
                }
                bombMode = true;
            }
            else{
                GameScene.bombLightOff();
            }
        }
    }

   public void handleFreeze() {
        timeLabel.pauseTime(10);
        freezeCount--;
    }

    public void handleHint() {
        if(hintPath.isEmpty()){
            // handleLose();
        }
        else{
            Crd c1 = hintPath.getFirst();
            Crd c2 = hintPath.getLast();
            board.showHint(c1,c2);
        }
    }

    public void handlePause() {
        sceneCtrl.setScene(new PauseScene(this));
        timeLabel.pauseTime();
    }

    // Pause Scene
    public void handleExitToLevelSelect() {
        audioCtrl.playButtonSound();
        handleSave(true);
        if (loadNumber != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save info");
            alert.setContentText("Game automatically saved!");
            alert.showAndWait();
        }
        showLevelScene();
    }

    public void handleRestart() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to restart?");
        alert.setContentText("All the unsaved data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gameSaveDao.delMapSave(loadNumber, currentLevel);
                showNewGameScene();
            }
        });
    }

    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }

    public void showLoadScene() {
        if(account == null) {
            loginCtrl.showAccountScene();
        } else{
            sceneCtrl.setScene(new LoadScene(this));
        }
    }

    public void backToLoadScene(){

    }

    public void showLoginScene() {
        sceneCtrl.setScene(new LoginScene(new LoginCtrl(userDao, audioCtrl, sceneCtrl, this)));
    }

    public void showLevelScene() {
        levelScene.playInfo(loadNumber);
        sceneCtrl.setScene(levelScene);
    }


    // save related variables
    private MapSaveData maps;
    private int bombCount, hintCount, freezeCount, eliminatedCount;

    public int getBombCount() {
        return bombCount;
    }

    public int getHintCount() {
        return hintCount;
    }

    public int getFreezeCount() {
        return freezeCount;
    }

    public void showNewGameScene() {
        int row = 12,col = 12;
        boolean isPair = false;
        try{
            maps = gameSaveDao.loadMaps(loadNumber);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Save Data");
            alert.setContentText("The game will create a new save");
            gameSaveDao.delMapSave(loadNumber, currentLevel);
            alert.showAndWait();
            return;
        }
        // load or reset info
        if (maps != null) {
            setLinkyMap(maps);
            bombCount = maps.getBombCount(currentLevel);
            eliminatedCount = maps.getEliminated(currentLevel);
            freezeCount = maps.getFreezeCount(currentLevel);
            hintCount = maps.getHintCount(currentLevel);
            System.out.println("map loaded");
            timeLabel = new TimeLabel(maps.getRemainTime(currentLevel), this);
            audioCtrl.setVolume(50.0);
            timeLabel.start();
            scoreLabel = new ScoreLabel(maps.getScore(currentLevel));
            progressLabel = new ProgressLabel(maps.getEliminated(currentLevel), MapSaveData.getTotal(currentLevel));
        } else {
            linkyMap = new LinkyMap(row, col, currentLevel, isPair);
            bombCount = 3;
            hintCount = 3;
            freezeCount = 3;
            System.out.println("Default map applied");
            audioCtrl.setVolume(50.0);
            timeLabel = new TimeLabel((currentLevel == 0) ? 180 : 300, this);
            scoreLabel = new ScoreLabel();
            progressLabel = new ProgressLabel(0, MapSaveData.getTotal(currentLevel));
            timeLabel.start();
            if(loadNumber != 0){
                handleSave(false);
                maps = gameSaveDao.loadMaps(loadNumber);
            }
        }
        hintPath = linkyMap.pathAutoFind();
        board = new Board(row, col, 36, linkyMap, this);
        gameScene = new GameScene(this);
        sceneCtrl.setScene(gameScene);
    }

    public void timeUp() {
        gameSaveDao.delMapSave(loadNumber, currentLevel);
        sceneCtrl.setScene(new LoseScene(this));
    }

    public void showWinScene(){
        gameSaveDao.delMapSave(loadNumber, currentLevel);
        sceneCtrl.setScene(new WinScene(this));
    }

    public void handleCellClick(CellNode cellNode) {
        audioCtrl.playClickSound();
        if (linkyMap.getMap()[cellNode.getCrd().x()][cellNode.getCrd().y()] != -1) {
            if (selectedCell == null) {
                selectedCell = cellNode;
                selectedCell.setHighlight(true);
                if (bombMode) {selectedCell.setBomb(true);}
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
                } else if (bombMode && cellNode.getType() != selectedCell.getType()) {
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

    public void eliminate(CellNode cellNode1, CellNode cellNode2, ArrayList<Crd> route)
    {
        cellNode1.setHighlight(true);
        cellNode2.setHighlight(true);
        progressLabel.eliminate();
        if (bombMode && bombCount > 0) {
            cellNode1.setBomb(true);
            cellNode2.setBomb(true);
            cellNode1.eliminateCell();
            cellNode2.eliminateCell();
            ArrayList<Crd> del = new ArrayList<>();
            del.add(cellNode1.getCrd());
            del.add(cellNode2.getCrd());
            linkyMap.delNumMap(del);
            audioCtrl.playBombSound();
            GameScene.bombLightOff();
            bombCount--;
            GameScene.updateBombBtn(bombCount);
            bombMode = false;
        } else {
            board.eliminate(cellNode1, cellNode2, route);
            audioCtrl.playEliminateSound();
        }
        linkyMap.delNumMap(route);
        InformationUtil.playInformation(sceneCtrl.getRoot(), "Eliminated:" + selectedCell.getType() + "x2!\n" + "Combo " + ++combo + "!\n Score " + (10 + 5 * (combo - 1)));
        selectedCell = null;
        scoreLabel.addScore(combo);
        if (linkyMap.isComplete()) {
            if(loadNumber != 0 && scoreLabel.getScore()>maps.getMaxScore(loadNumber, currentLevel)){
                maps.setMaxScore(loadNumber,currentLevel, scoreLabel.getScore());
            }
            levelScene.unlock(currentLevel, loadNumber);
            showWinScene();
            return;
        }
        hintPath = linkyMap.pathAutoFind();
        if(hintPath.isEmpty() && bombCount == 0){
            // handleLose();
        }
    }
}
