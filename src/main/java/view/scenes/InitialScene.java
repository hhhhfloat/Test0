package view.scenes;


import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.InitialBox;

public class InitialScene extends Scene{
    public InitialScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 600);
        getStylesheets().add(getClass().getResource("/css/initialSceneStyle.css").toExternalForm());
    }
    private static StackPane createRoot(LoginCtrl loginCtrl) {
        return new StackPane(new InitialBox(loginCtrl));
    }
}
