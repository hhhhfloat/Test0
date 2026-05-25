package controller;

import dao.GameSaveDao;
import dao.UserDao;
import dao.impl.FileGameSaveDao;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Account;
import model.entity.MapSaveData;
import model.state.ScoreEntry;
import view.scenes.*;

import java.util.List;

public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final SceneCtrl sceneCtrl;
    public final int totLoadNumber = 3;
    private Account account;
    private boolean isTourist;
    private int loadNumber = 0;

    private GameCtrl gameCtrl;
    private LevelSelectScene levelSelectScene;
    private GameSaveDao gameSaveDao;

    private MapSaveData mapsForTourist;

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.sceneCtrl = sceneCtrl;
    }

    public boolean isTourist() {
        return isTourist;
    }


    public void setGameCtrl(GameCtrl gameCtrl){
        this.gameCtrl = gameCtrl;
    }

    public Account getAccount() {
        return account;
    }

    public int getLoadNumber() {
        return loadNumber;
    }

    public void syncHighestScore(){
        int maxScore = 0;
        for(int i = 1;i<=totLoadNumber;i++){
            MapSaveData maps = gameSaveDao.loadSelectedLoad(i);
            int sum = 0;
            if(maps != null){
                int[] scores = maps.getMaxHistoryScore();
                for (int score : scores) {
                    sum += score;
                }
                maxScore = Math.max(maxScore, sum);
            }
        }
        userDao.updateHighScore(account.getUserName(),maxScore);
    }

    public void handleLogin() {
        audioCtrl.playButtonSound();
        showLoginScene();
    }
    public void handleLoginCancel() {
        showInitialScene();
        audioCtrl.playButtonSound();
    }
    public void handleLoginConfirm(String username, String password) {
        audioCtrl.playButtonSound();
        if (username.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username can't be null!");
            alert.showAndWait();
        } else if (!userDao.existForLogin(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Username doesn't exist!");
            alert.showAndWait();
        }
        else if (userDao.validate(username, password)) {
            Alert alert;
            account = userDao.findByUsername(username);
            if(account == null){
                userDao.deleteAccount(username);
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("INVALID USER INFORMATION");
                alert.showAndWait();
                return;
            }
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Login succeeded!");
            alert.showAndWait();
            isTourist = false;
            loadNumber = 0;
            showAccountScene();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Wrong password");
            alert.showAndWait();
        }

    }

    public void handleTouristMode() {
        isTourist=true;
        audioCtrl.playButtonSound();
        showAccountScene();
    }


    private static final String SAFE_NAME_PATTERN = "[\\\\/:*?\"<>|\\p{Cntrl}]";
    public static String properName(String s){
        String cleaned = s.replaceAll(SAFE_NAME_PATTERN,"_");
        if(cleaned.startsWith(".")||cleaned.startsWith("-")){
            cleaned = "_"+cleaned;
        }
        return cleaned;
    }
    public void handleRegister() {
        audioCtrl.playButtonSound();
        showRegisterScene();
    }

    public void handleRegisterCancel() {
        audioCtrl.playButtonSound();
        sceneCtrl.setScene(new LoginScene(this));
    }

    public void handleRegisterConfirm(String username, String password, String confirmPwd) {
        audioCtrl.playButtonSound();
        if(username.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Username can't be empty");
            alert.showAndWait();
            return;
        }
        String safeUsername = properName(username);
        if(safeUsername.length()>1000){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please use a shorter name!\n***Shorten your name with \n.&❂*…←…鳼№茡洟丗▦©∭");
            alert.showAndWait();
        }else if(safeUsername.length() > 200) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please use a shorter name!");
            alert.showAndWait();
        }
        else if(userDao.existForRegister(username)){
            System.out.println("Check");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Username already exists in safe format: " + safeUsername);
            alert.showAndWait();
        }
        else if(password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please set up your password");
            alert.showAndWait();
        }else if(!password.equals(confirmPwd)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Password do not match");
        } else {
            userDao.createUser(username, password);
            account = new Account(username);
            account.setPassword(password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Register succeeded");
            alert.showAndWait();
            isTourist = false;
            loadNumber = 0;
            showAccountScene();
        }
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

    public void handleStart() {
        audioCtrl.playButtonSound();
        if (isTourist) { /// 游客模式登录
            gameCtrl = new GameCtrl(sceneCtrl, audioCtrl,this, null);
            if(mapsForTourist == null){
                mapsForTourist = new MapSaveData();
            }
            gameCtrl.setMaps(mapsForTourist);
            levelSelectScene = gameCtrl.getLevelSelectScene();
            int maxUnlockedLevel = mapsForTourist.getMaxUnlockedLevel();
            if(maxUnlockedLevel != 0){
                levelSelectScene.setUnlockedLevel(maxUnlockedLevel);
            }
            showLevelSelectScene(false);
        } else {
            if(mapsForTourist != null)mapsForTourist = null;
            showLoadScene();
        }
    }
    private int[] maxScoreForTourist = new int[LevelSelectScene.getTotLevelNumber()];
    public boolean setMaxScore(int currentLevel, int score) {
        if(score>maxScoreForTourist[currentLevel]){
            maxScoreForTourist[currentLevel] = score;
            return true;
        }
        return false;
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Sure to leave?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loadNumber = 0;
                account = null;
                gameCtrl = null;
                levelSelectScene = null;
                if(mapsForTourist != null)mapsForTourist = null;
                maxScoreForTourist = new int[LevelSelectScene.getTotLevelNumber()];
                showInitialScene();
            }
        });
    }

    public void handleLeaderboard() {
        audioCtrl.playButtonSound();
        String userInfo = "";
        VBox list = new VBox(10);
        List<ScoreEntry> userList = userDao.getLeaderboard(30);
        for (int i = 1; i <= userList.toArray().length; i++) {
            String info = String.format("No.%d: %s   Score:%d", i, userList.get(i-1).getName(), userList.get(i-1).getScore());
            if(!isTourist && account.getUserName().equals(userList.get(i-1).getName())) {
                userInfo = info;
            }
            Label menuItem = new Label(info);
            menuItem.setMaxWidth(Double.MAX_VALUE);
            menuItem.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
            list.getChildren().add(menuItem);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(300, 300);

        Label label = new Label(userInfo);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");

        VBox vBox = !isTourist ? new VBox(30, scrollPane, label) : new VBox(scrollPane);

        Scene scene = new Scene(vBox);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setScene(scene);
        leaderboardStage.show();
    }

    public void handleLoad(int k) {
        audioCtrl.playButtonSound();
        loadNumber = k;
        gameCtrl = new GameCtrl(sceneCtrl, audioCtrl, this, gameSaveDao);
        levelSelectScene = gameCtrl.getLevelSelectScene();
        gameCtrl.loadGame();
        showLevelSelectScene(false);
    }

    public void handleLoadDelete(int k){
        audioCtrl.playButtonSound();
        loadNumber = k;
        GameCtrl.deleteSave(k);
    }

    public void showLevelSelectScene(boolean isNewUnlock) {
        if(isNewUnlock){
            levelSelectScene.playInfo();
        }
        sceneCtrl.setScene(levelSelectScene);
    }
    public void showLoadScene() {
        sceneCtrl.setScene(new LoadScene(this));
    }
    public void showInitialScene() {
        sceneCtrl.setScene(new InitialScene(this));
    }
    public void showLoginScene() {
        sceneCtrl.setScene(new LoginScene(this));
    }
    public void showRegisterScene() {
        sceneCtrl.setScene(new RegisterScene(this));
    }
    public void showAccountScene() {
        if(!isTourist){
            gameSaveDao = new FileGameSaveDao();
            gameSaveDao.setCurrentUser(account);
            syncHighestScore();
        }
        sceneCtrl.setScene(new AccountScene(this));
    }


}
