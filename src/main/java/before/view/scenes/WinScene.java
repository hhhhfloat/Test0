package before.view.scenes;

import before.controller.GameCtrl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import modifying.auth.view.tools.MouseGlowEffect;
import before.view.boxes.WinBox;
import java.nio.file.Paths;
import java.util.Random;

public class WinScene extends Scene {
    public WinScene(GameCtrl gameCtrl) {
        super(new StackPane(), 800, 800);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "SceneStyle", "loseSceneStyle.css").toUri().toString());
        WinBox winBox = new WinBox(gameCtrl);
        StackPane root = (StackPane)getRoot();
        root.getChildren().add(winBox);

        if(gameCtrl.isNewRecord()){
            Label newRecord = newRecordLabel();
            root.getChildren().add(newRecord);

            newRecord.setTranslateX(200);   // 右侧留出空间
            newRecord.setTranslateY(-100);                     // 标题下方适当位置
            newRecord.setRotate(-20);

            root.parentProperty().addListener((obs,oldParent,newParent)->{
                if(newParent == null && colorCycle != null){
                    colorCycle.stop();
                }
            });
        }

        MouseGlowEffect.attach(this, (StackPane) getRoot());
    }
    private Timeline colorCycle;
    private final String[] fancyFonts={
            "Agency FB","Algerian","Bauhaus 93","Bernard MT Condensed","Book Antiqua", "Copperplate Gothic Bold"
    };
    public Label newRecordLabel(){
        Label newRecord = new Label("★ NEW RECORD ★");
        newRecord.getStyleClass().add("new-record");
        String randomFont = fancyFonts[new Random().nextInt(0,fancyFonts.length)];
        newRecord.setStyle("-fx-font-family: '" + randomFont + "';");
        double[] hue = {0};

        colorCycle = new Timeline(
                new KeyFrame(Duration.millis(20), e->{
                    hue[0] = (hue[0]+2)%360;
                    double alpha = 0.6 + Math.sin(Math.toRadians(hue[0] * 2)) * 0.3;
                    Color newcolor = Color.hsb(hue[0], 0.9,0.9,alpha);
                    newRecord.setTextFill(newcolor);
                })
        );

        colorCycle.setCycleCount(Animation.INDEFINITE);
        colorCycle.play();
        return newRecord;
    }
}
