package modifying.game.controller;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modifying.shared.model.TOAST_TYPE;
import modifying.game.view.sceneroots.GameSelectView;
import modifying.game.view.tools.MouseGameEffect;
import modifying.shared.controller.MainController;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GameSceneCtrl {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;
    private static final double LOGIC_WIDTH = 800;
    private static final double LOGIC_HEIGHT = 800;

    private final MainController mainController;
    private final Stage gameStage;


    private final StackPane gameRoot;
    private final Pane mouseEffectPane;
    private final StackPane contentPane;
    private final Group messageGroup;
    private final Group rootGroup;
    private final StackPane messagePane;
    private final StackPane permanentRoot;
    private final StackPane contentContainer;
    private final Scene gameScene;
    private final Map<String, Parent> viewCache = new HashMap<>();

    private GameSelectView gameSelectView;

    public GameSceneCtrl(Stage gameStage, MainController mainController){
        this.gameStage = gameStage;
        this.mainController = mainController;

        gameRoot = new StackPane();
        gameScene = new Scene(gameRoot, LOGIC_WIDTH,LOGIC_HEIGHT);
        gameScene.getStylesheets().add(Paths.get("src","main","resources","css","GameSceneStyle","GameSelectSceneStyle.css").toUri().toString());
        gameStage.setScene(gameScene);

        mouseEffectPane = new Pane();
        mouseEffectPane.setMouseTransparent(true);
        mouseEffectPane.setPickOnBounds(false);
        mouseEffectPane.prefWidthProperty().bind(gameRoot.widthProperty());
        mouseEffectPane.prefHeightProperty().bind(gameRoot.heightProperty());
        gameRoot.getChildren().add(mouseEffectPane);
        MouseGameEffect.attach(gameScene, mouseEffectPane);

        messagePane = new StackPane();
        messagePane.setPrefSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMinSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMaxSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        messagePane.setMouseTransparent(true);

        messageGroup = new Group(messagePane);
        messageGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(()->{
                    double w = gameScene.getWidth();
                    double h = gameScene.getHeight();
                    if(w<=0||h<=0)return 1.0;
                    return Math.min(w/LOGIC_WIDTH,h/LOGIC_HEIGHT);
                }, gameScene.widthProperty(), gameScene.heightProperty())
        );
        messageGroup.scaleYProperty().bind(messageGroup.scaleXProperty());

        contentPane = new StackPane();
        contentPane.minWidthProperty().bind(gameScene.widthProperty());
        contentPane.prefWidthProperty().bind(gameScene.widthProperty());
        contentPane.maxWidthProperty().bind(gameScene.widthProperty());
        contentPane.minHeightProperty().bind(gameScene.heightProperty());
        contentPane.prefHeightProperty().bind(gameScene.heightProperty());
        contentPane.maxHeightProperty().bind(gameScene.heightProperty());
        contentPane.getStylesheets().add(Paths.get("src","main","resources","css","GameSceneStyle","confirmStyle.css").toUri().toString());
        contentPane.getStylesheets().add(Paths.get("src","main","resources","css","GameSceneStyle","toastStyle.css").toUri().toString());
        gameRoot.getChildren().add(contentPane);

        permanentRoot = new StackPane();
        permanentRoot.setPrefSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        permanentRoot.setMinSize(LOGIC_WIDTH, LOGIC_HEIGHT);
        permanentRoot.setMaxSize(LOGIC_WIDTH, LOGIC_HEIGHT);

        contentContainer = new StackPane();
        permanentRoot.getChildren().add(contentContainer);

        rootGroup = new Group(permanentRoot);
        contentPane.getChildren().add(rootGroup);
        contentPane.getChildren().add(messageGroup);

        rootGroup.scaleXProperty().bind(
                Bindings.createDoubleBinding(()->{
                    double w = gameScene.getWidth();
                    double h = gameScene.getHeight();
                    if(w<=0||h<=0)return 1.0;
                    return Math.min(w/LOGIC_WIDTH,h/LOGIC_HEIGHT);
                }, gameScene.widthProperty(), gameScene.heightProperty())
        );
        rootGroup.scaleYProperty().bind(rootGroup.scaleXProperty());

    }

    public void showGameSelectScene(GameCtrl gameCtrl, String toastMessage, TOAST_TYPE toastType){
        gameSelectView = (GameSelectView) viewCache.computeIfAbsent("GAMESELECT",k->new GameSelectView(gameCtrl));
    }
    public void showGameSelectScene(GameCtrl gameCtrl){
        showGameSelectScene(gameCtrl,null,null);
    }


}
