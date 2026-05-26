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
import java.nio.file.Paths;
import java.util.List;

public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final SceneCtrl sceneCtrl;
    public final int totLoadNumber = 3;
    private Account account;
    private boolean isTourist;
    private int loadNumber = 0;
    Alert warningAlert= new Alert(Alert.AlertType.WARNING), infoAlert = new Alert(Alert.AlertType.INFORMATION), confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);

    private GameCtrl gameCtrl;
    private LevelSelectScene levelSelectScene;
    private GameSaveDao gameSaveDao;

    private MapSaveData mapsForTourist;

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.sceneCtrl = sceneCtrl;
        String path = Paths.get("src", "main", "resources", "css", "infoStyle.css").toUri().toString();
        warningAlert.setTitle("Warning");
        warningAlert.getDialogPane().getStylesheets().add(path);
        infoAlert.setTitle("Information");
        infoAlert.getDialogPane().getStylesheets().add(path);
        confirmAlert.setTitle("Confirm");
        confirmAlert.getDialogPane().getStylesheets().add(path);
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

    public void showWarningAlert(String string) {
        warningAlert.setContentText(string);
        warningAlert.showAndWait();
    }

    public void showInfoAlert(String string) {
        infoAlert.setContentText(string);
        infoAlert.showAndWait();
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
            showWarningAlert("Username can't be null!");
        } else if (!userDao.existForLogin(username)) {
            showWarningAlert("Username doesn't exist!");
        }
        else if (userDao.validate(username, password)) {
            account = userDao.findByUsername(username);
            if(account == null){
                userDao.deleteAccount(username);
                showWarningAlert("INVALID USER INFORMATION");
                return;
            }
            showInfoAlert("Login succeeded!");
            isTourist = false;
            loadNumber = 0;
            showAccountScene();
        } else {
            showWarningAlert("Wrong password!");
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
            showWarningAlert("Username can't be empty");
            return;
        }
        String safeUsername = properName(username);
        if(safeUsername.length()>1000){
            showWarningAlert("Please use a shorter name!\n***Shorten your name with \n.&❂*…←…鳼№茡洟丗▦©∭");
        }else if(safeUsername.length() > 200) {
            showWarningAlert("Please use a shorter name!");
        }
        else if(userDao.existForRegister(username)){
            System.out.println("Check");
            showWarningAlert("Username already exists in safe format: " + safeUsername);
        }
        else if(password.isEmpty()){
            showWarningAlert("Please set up your password");
        }else if(!password.equals(confirmPwd)){
            showWarningAlert("Password do not match");
        } else {
            userDao.createUser(username, password);
            account = new Account(username);
            account.setPassword(password);
            showInfoAlert("Register succeeded");
            isTourist = false;
            loadNumber = 0;
            showAccountScene();
        }
    }

    public void handleExit() {
        audioCtrl.playButtonSound();
        confirmAlert.setHeaderText("Are you sure you want to exit?");
        confirmAlert.setContentText("All the unsaved data will be lost!");
        confirmAlert.showAndWait().ifPresent(response -> {
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
    private final int[] maxScoreForTourist = new int[LevelSelectScene.getTotLevelNumber()];
    public boolean setMaxScore(int currentLevel, int score) {
        if(score>maxScoreForTourist[currentLevel]){
            maxScoreForTourist[currentLevel] = score;
            return true;
        }
        return false;
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        confirmAlert.setTitle("Confirm");
        confirmAlert.setHeaderText("Sure to leave?");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loadNumber = 0;
                account = null;
                gameCtrl = null;
                levelSelectScene = null;
                if(mapsForTourist != null)mapsForTourist = null;
                //maxScoreForTourist = new int[LevelSelectScene.getTotLevelNumber()];
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
