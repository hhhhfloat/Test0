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
import model.entity.LinkyMap;
import view.game_nodes.Board;
import view.game_nodes.BoardInterface;
import view.game_nodes.CellNode;
import view.scenes.*;

import java.util.ArrayList;

public class GameCtrl {
    private final UserDao userDao;
    private final SceneCtrl sceneCtrl;
    private BoardInterface board;
    private Account account;
    private LinkyMap linkyMap;
    private CellNode selectedCell;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
        selectedCell = null;
    }

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

    public void handleCellClick(CellNode cellNode) {
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
                } else {
                    cellNode.setHighlight(true);
                    board.eliminate(selectedCell, cellNode, route);
                    selectedCell = null;
                    /// 新增：更新四维数表
                    linkyMap.delNumMap(route);
                }
            }
        }
    }

    public void handlePause() {

    }

    public void handleSave() {

    }

    public void handleExitToMenu() {

    }

    public void handleRestart() {

    }

    public void handleContinue() {

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

    private void setLinkyMap(int row, int col, int mode, boolean isPair) {
        linkyMap = new LinkyMap(row, col, mode, isPair);
        Board boardImpl = new Board(row, col, 50, linkyMap, this);
        this.board = boardImpl;
    }

    public void showGameScene(int mode) {
        if(mode == 0){
            setLinkyMap(12, 12, 0, false);
            sceneCtrl.setScene(new GameScene(board));
        } else {
            setLinkyMap(12, 12, 1, false);
            sceneCtrl.setScene(new GameScene(board));
        }
    }
}
