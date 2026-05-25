package controller;

import dao.GameSaveDao;
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

public class GameCtrl extends Parent {
    // 用户使用部分
    private final Account account;
    private static GameSaveDao gameSaveDao = null;
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
    private final boolean isTourist;

    public GameCtrl( SceneCtrl sceneCtrl, AudioCtrl audioCtrl, LoginCtrl loginCtrl,GameSaveDao gameSaveDao) {
        this.sceneCtrl = sceneCtrl;
        this.audioCtrl = audioCtrl;
        this.loginCtrl = loginCtrl;
        this.loadNumber = loginCtrl.getLoadNumber();
        levelSelectScene = new LevelSelectScene(this);

        /// initialize gameSaveDao
        account = loginCtrl.getAccount();
        isTourist = (account == null);
        selectedCell = null;
        /// 游客数据在loginCtrl管理
        if(!isTourist){
            GameCtrl.gameSaveDao = gameSaveDao;
            GameCtrl.gameSaveDao.setGameCtrl(this);
        }
    }

    public boolean isTourist() {
        return isTourist;
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
        if(isTourist){
            alert.setContentText("Game will be automatically saved");
        }
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

    public void loadGame() {
        try{
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
            return;
        }
        int maxUnlockedLevel = maps.getMaxUnlockedLevel();
        if(maxUnlockedLevel != 0){
            levelSelectScene.setUnlockedLevel(maxUnlockedLevel);
        }
    }

    private final boolean[] levelIsPair = {
            false, false, false, false, false};

    public void handleLevel(int ind) {
        audioCtrl.playButtonSound();
        currentLevel = ind;
        if(currentLevel < maps.getMaxUnlockedLevel())isNewUnlock = false;
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
        // save map
        if(linkyMap != null){
            maps.setMap(currentLevel, linkyMap.getMap());
            maps.setScore(currentLevel, scoreLabel.getScore());
            maps.setEliminated(currentLevel, eliminatedCount);
            maps.setFreezeCount(currentLevel, freezeCount);
            maps.setBombCount(currentLevel, bombCount);
            maps.setRemainTime(currentLevel, timeLabel.getRemainingTime());
        }
        handleLeaveLoad(loadNumber);
        if(isShowed){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminding");
            alert.setHeaderText("Game saved" + (isTourist?" temporarily":""));
            alert.showAndWait();
        }
    }

    public void handleLeaveLoad(int loadNumber){
        maps.setMaxUnlockedLevel(levelSelectScene.getMaxUnlocked());
        if (!isTourist) {
            gameSaveDao.saveCurrentLoad(maps, loadNumber);
            loginCtrl.syncHighestScore();
        }
    }

    public static void deleteSave(int loadNumber){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Are you sure to delete this save?");
        alert.setContentText("It will be lost forever! (A long time!)");
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
            handleLeaveLoad(loadNumber);
            loginCtrl.showLoadScene();
        }
    }

    //Game Scene
    public void handleBombMode() {
        audioCtrl.playButtonSound();
        clearCombo();
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

    boolean isFreeze = false;

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
        GameScene.freezeTime(isFreeze);
    }
    public void playIceSound(){
        audioCtrl.playIceBreakSound();
    }

    public void handleFreeze() {
        clearCombo();
        audioCtrl.playButtonSound();
        if(!isFreeze){
            freezeCount--;
            setFreeze(true);
            GameScene.updateFreezeBtn(freezeCount);
            timeLabel.pauseTime(10);
        }
    }

    public void handleLose() {
        audioCtrl.playButtonSound();
        isNewUnlock = false;
        sceneCtrl.setScene(new LoseScene(this));
    }

    public void handleHint() {
        clearCombo();
        if(hintPath.isEmpty() && bombCount <= 0){
            handleLose();
        }
        else if(hintCount > 0){
            Crd c1 = hintPath.getFirst();
            Crd c2 = hintPath.getLast();
            board.showHint(c1,c2);
            hintCount--;
            GameScene.updateHintBtn(hintCount);
        }
    }

    public void handlePause() {
        clearCombo();
        sceneCtrl.setScene(new PauseScene(this));
        timeLabel.pauseTime();
    }

    // Pause Scene
    public void handleExitToLevelSelect() {
        isNewUnlock = false;
        timeLabel.pauseTime();
        audioCtrl.playButtonSound();
        handleSave(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save info");
        alert.setContentText("Game automatically saved!");
        alert.showAndWait();
        showLevelSelectScene();
    }

    public void handleRestart() {
        audioCtrl.playButtonSound();
        timeLabel.pauseTime();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Are you sure you want to restart?");
        alert.setContentText("All the unsaved data will be lost!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if(isTourist){
                    resetMaps();
                } else{
                    gameSaveDao.delSelectedLevelSave(maps, currentLevel);
                }
                showNewGameScene();
            }
        });
    }
    public void resetMaps(){
        maps.setMap(currentLevel, new int[0][0]);
        maps.setScore(currentLevel, 0);
        maps.setEliminated(currentLevel,0);
        maps.setBombCount(currentLevel,3);
        maps.setFreezeCount(currentLevel,3);
        maps.setHintCount(currentLevel, 3);
        maps.setRemainTime(currentLevel);
    }

    public void handleContinue() {
        sceneCtrl.setScene(gameScene);
        timeLabel.continueTime();
    }

    private boolean isNewUnlock;

    public boolean isNewUnlock() {
        return isNewUnlock;
    }

    public void showLevelSelectScene() {
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
        if(isTourist){
            resetMaps();
        }else{
            gameSaveDao.delSelectedLevelSave(maps, currentLevel);
        }
        sceneCtrl.setScene(new LoseScene(this));
        audioCtrl.setVolume(0.5);
        audioCtrl.playBombSound();
        audioCtrl.setVolume(1.0);
    }

    public void showWinScene(){
        try{
            Thread.sleep(100);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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
                clearCombo();
                cellNode.setHighlight(false);
                selectedCell.setBomb(false);
                selectedCell = null;
            } else {
                ArrayList<Crd> route = linkyMap.pathFindByPoint(selectedCell.getCrd(), cellNode.getCrd());
                if (!bombMode && route.isEmpty()) {
                    selectedCell.setHighlight(false);
                    cellNode.setHighlight(true);
                    selectedCell = cellNode;
                } else if (bombMode && cellNode.getType() != selectedCell.getType()) {
                    clearCombo();
                    selectedCell.setBomb(false);
                    selectedCell.setHighlight(false);
                    cellNode.setBomb(true);
                    selectedCell = cellNode;
                } else {
                    eliminate(selectedCell, cellNode, route);
                    eliminatedCount++;
                }
            }
        }
    }

    public void clearCombo(){
        combo = 0;
        GameScene.playInfo("Combo out! 😣");
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
        String s = "2 ×" + selectedCell.getName() + "! " + (++combo) + " COMBO"  + "!\n+" + (10 + 5 * (combo - 1)+" score");
        GameScene.playInfo(s);
        selectedCell = null;
        scoreLabel.addScore(combo);
        if (linkyMap.isComplete()) {
            timeLabel.pauseTime();
            combo = 0;
            if(loadNumber != 0)
            {
                if(scoreLabel.getScore()>maps.getMaxScore(currentLevel)){
                    maps.setMaxScore(currentLevel, scoreLabel.getScore());
                    GameScene.playInfo("*** NEW RECORD ***");
                }
                gameSaveDao.delSelectedLevelSave(maps, currentLevel);
            }
            if(levelSelectScene.getMaxUnlocked()<=currentLevel){
                isNewUnlock = true;
                levelSelectScene.unlock(currentLevel);
            }
            showWinScene();
            return;
        }
        hintPath = linkyMap.pathAutoFind();
        if(hintPath.isEmpty() && bombCount == 0){
            combo = 0;
            isNewUnlock = false;
            handleLose();
        }
    }

    public void setMaps(MapSaveData mapsForTourist) {
        this.maps = mapsForTourist;
    }

    public void setMaxUnlocked(int unlockIndex) {
        maps.setMaxUnlockedLevel(unlockIndex);
        if(!isTourist){
            gameSaveDao.saveCurrentLoad(maps, loadNumber);
        }
    }
}
