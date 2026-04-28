package controller;

import dao.UserDao;
import dao.impl.FileUserDao;
import model.entity.Crd;

public class GameCtrl {
    private UserDao userDao = new FileUserDao();
    private SceneCtrl sceneCtrl;

    public GameCtrl(UserDao userDao, SceneCtrl sceneCtrl) {
        this.userDao = userDao;
        this.sceneCtrl = sceneCtrl;
    }
    public void handleCellClick(Crd p) {

    }

    public void handlePause() {

    }

    public void handleSave() {

    }

    public void handleExitToMenu() {

    }

    public void handleExit() {

    }

    public void handleRestart() {

    }

    public void handleEasy() {

    }

    public void handleDifficult() {

    }

    public int getCurrentScore() {
        int score = 0;
        return score;
    }

    public int getRemainTime() {
        int time = 0;
        return time;
    }



    // 供view刷新用
    public boolean isGameRunning() {
        return true;
    }


    public void handleLoad() {

    }

}
