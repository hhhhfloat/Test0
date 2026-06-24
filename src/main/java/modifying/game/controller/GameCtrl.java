package modifying.game.controller;

import modifying.shared.controller.AudioCtrl;
import modifying.shared.model.MapSaveData;

public class GameCtrl {

    private final GameSceneCtrl gameSceneCtrl;
    private final AudioCtrl audioCtrl;
    private final String username;

    private final MapSaveData mapSaveData;

    public GameCtrl(GameSceneCtrl gameSceneCtrl, AudioCtrl audioCtrl, MapSaveData mapSaveData, String username) {
        this.gameSceneCtrl = gameSceneCtrl;
        this.audioCtrl = audioCtrl;
        this.username = username;
        this.mapSaveData = mapSaveData;
    }

    public void launchGame(String gameId){
        System.out.println("Launching" + gameId);
    }
}
