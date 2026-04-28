package view.scenes;

import controller.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import view.boxes.LoginBox;

public class LoginScene extends Scene {
    public LoginScene(LoginCtrl loginCtrl) {
        super(createRoot(loginCtrl), 800, 600);
    }

    private static StackPane createRoot(LoginCtrl loginCtrl) {
        StackPane root = new StackPane();

        //root.setBackground(BgImage.getBgImage());

        LoginBox loginBox = new LoginBox(loginCtrl);
        root.getChildren().add(loginBox);

        return root;
    }
}
