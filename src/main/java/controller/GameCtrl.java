package controller;

import dao.GameSaveDao;
import dao.impl.FileGameSaveDao;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private final Account account;
    private final GameSaveDao gameSaveDao;
    // controller 连接部分
    private final SceneCtrl sceneCtrl;
    private final AudioCtrl audioCtrl;
    private final LoginCtrl loginCtrl;
    // 持有view与model引用
    private BoardInterface board;
    private TimeLabelInterface timeLabel;
    private ScoreLabelInterface scoreLabel;
    private ProgressLabelInterface progressLabel;

    private LinkyMap linkyMap;

    private final LevelSelectScene levelSelectScene;
    private GameScene gameScene;
    // 辅助
    private CellNode selectedCell;
    private int currentLevel;
    private int combo = 0;
    private final int loadNumber;
    private boolean bombMode = false;
    private ArrayList<Crd> hintPath;

    public GameCtrl( SceneCtrl sceneCtrl, AudioCtrl audioCtrl, LoginCtrl loginCtrl) {
        this.sceneCtrl = sceneCtrl;
        this.audioCtrl = audioCtrl;
        this.loginCtrl = loginCtrl;
        this.loadNumber = loginCtrl.getLoadNumber();
        levelSelectScene = new LevelSelectScene(this);

        /// initialize gameSaveDao
        account = loginCtrl.getAccount();
        selectedCell = null;
        gameSaveDao = new FileGameSaveDao();
        gameSaveDao.setGameCtrl(this);
        if(loginCtrl.isTourist()) { /// 游客登录
            handleLoad0();
        }else{
            gameSaveDao.setCurrentUser(account.getUserName());
        }
    }

    public LevelSelectScene getLevelSelectScene() {
        return levelSelectScene;
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

    public int getLoadNumber() {
        return loadNumber;
    }

    // Loading Works
    public void handleLoad0() {
        showLevelSelectScene(false);
    }

    public void loadGame() {
        try{
            System.out.println(loadNumber);
            maps = gameSaveDao.loadSelectedLoad(loadNumber);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Save Data");
            alert.setContentText("The game will create a new save");
            gameSaveDao.delLoadSave(loadNumber);
            alert.showAndWait();
            return;
        }
        if(maps == null) {
            maps = new MapSaveData();
            System.out.println("null");
            return;
        }
        int maxUnlockedLevel = maps.getMaxUnlockedLevel();
        levelSelectScene.setUnlockedLevel(maxUnlockedLevel);
    }

    private final boolean[] levelIsPair = {
            false, false, false, false, false};

    public void handleLevel(int ind) {
        audioCtrl.playButtonSound();
        currentLevel = ind;
        showNewGameScene();
    }
    public void showNewGameScene() {
        int row = 12,col = 12;
        setLinkyMap();
        setUtilCount();
        setGameInfo();
        initGameNodes();
        hintPath = linkyMap.pathAutoFind();
        board = new Board(row, col, 36, linkyMap, this);
        gameScene = new GameScene(this);
        timeLabel.start();
        sceneCtrl.setScene(gameScene);
    }
    public void setLinkyMap(){
        int row = 12,col =12;
        boolean isPair = levelIsPair[currentLevel];
        int[][] levelMap = maps.getMap(currentLevel);
        if(levelMap.length == row && levelMap[0].length == col){
            System.out.println("Map save loaded");
            linkyMap = new LinkyMap(row, col, levelMap);
        }else {
            System.out.println("Default map applied for this level");
            linkyMap = new LinkyMap(row, col, currentLevel, isPair);
        }
    }
    public void setUtilCount(){
        bombCount = maps.getBombCount(currentLevel);
        freezeCount = maps.getFreezeCount(currentLevel);
        hintCount = maps.getHintCount(currentLevel);
    }
    public void setGameInfo(){
        eliminatedCount = maps.getEliminated(currentLevel);
    }
    public void initGameNodes(){
        timeLabel = new TimeLabel(maps.getRemainTime(currentLevel), this);
        scoreLabel = new ScoreLabel(maps.getScore(currentLevel));
        progressLabel = new ProgressLabel(eliminatedCount, MapSaveData.getTotalPairs(currentLevel));
    }

    public void handleSave(boolean isShowed) {
        if (loadNumber == 0 || account == null) {
            if(isShowed){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reminding");
                alert.setHeaderText("You can't save in visitor mode!");
                alert.showAndWait();
            }
            return;
        }
        // save map
        if(linkyMap != null){
            maps.setMap(currentLevel, linkyMap.getMap());
            maps.setScore(currentLevel, scoreLabel.getScore());
            maps.setEliminated(currentLevel, eliminatedCount);
            maps.setFreezeCount(currentLevel, freezeCount);
            maps.setBombCount(currentLevel, bombCount);
            maps.setRemainTime(currentLevel, timeLabel.getRemainingTime());
        }
        maps.setMaxUnlockedLevel(levelSelectScene.getMaxUnlocked());
        gameSaveDao.saveCurrentLoad(maps, loadNumber);

        if(isShowed){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminding");
            alert.setHeaderText("Game saved!");
            alert.showAndWait();
        }
    }

    public void handleLoadDelete(int loadNumber) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to delete this load?");
        alert.setContentText("All the data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gameSaveDao.delLoadSave(loadNumber);
            }
        });
    }


    public void handleBackFromLevelSelect() {
        audioCtrl.playButtonSound();
        if(loadNumber == 0 || account == null){
            loginCtrl.showAccountScene();
        }else{
            handleSave(false);
            loginCtrl.showLoadScene();
        }
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

    public void handleLose() {
        sceneCtrl.setScene(new LoseScene(this));
    }

    public void handleHint() {
        if(hintPath.isEmpty()){
            handleLose();
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
        handleSave(false);
        if (loadNumber != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save info");
            alert.setContentText("Game automatically saved!");
            alert.showAndWait();
        }
        showLevelSelectScene(false);
    }

    public void handleRestart() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to restart?");
        alert.setContentText("All the unsaved data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gameSaveDao.delSelectedLevelSave(maps, currentLevel);
                showNewGameScene();
            }
        });
    }

    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }




    public void showLevelSelectScene(boolean isNewUnlock) {
        loginCtrl.showLevelSelectScene(isNewUnlock);
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



    public void timeUp() {
        gameSaveDao.delSelectedLevelSave(maps, currentLevel);
        sceneCtrl.setScene(new LoseScene(this));
    }

    public void showWinScene(){
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
        GameScene.playInfo(++combo, selectedCell.getType());
        // InformationUtil.playInformation(gameScene.getRoot(), "Eliminated:" + selectedCell.getType() + "x2!\n" + "Combo " + ++combo + "!\n Score " + (10 + 5 * (combo - 1)));
        selectedCell = null;
        scoreLabel.addScore(combo);
        if (linkyMap.isComplete()) {
            if(loadNumber != 0)
            {
                if(scoreLabel.getScore()>maps.getMaxScore(currentLevel)){
                    maps.setMaxScore(currentLevel, scoreLabel.getScore());
                }
                gameSaveDao.delSelectedLevelSave(maps, currentLevel);
                // set highest score
            }
            levelSelectScene.unlock(currentLevel);
            showWinScene();
            return;
        }
        hintPath = linkyMap.pathAutoFind();
        if(hintPath.isEmpty() && bombCount == 0){
            handleLose();
        }
    }
}
