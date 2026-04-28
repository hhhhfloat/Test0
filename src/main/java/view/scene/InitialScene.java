package view.scene;


import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import view.scene.Boxes.InitialBox;
import view.scene.rsc.BgImage;

public class InitialScene {
    public static Scene initialScene() {
        BorderPane borderPane = new BorderPane();
        BgImage bgImage = new BgImage("src/main/resources/Sprites/bg.png");
        borderPane.setBackground(bgImage.getBgImage());
        Scene scene = new Scene(InitialBox.initialBtnBox(), bgImage.getWidth(), bgImage.getHeight());
        return scene;
    }
}
