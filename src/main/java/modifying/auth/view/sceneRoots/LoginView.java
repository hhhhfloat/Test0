package modifying.auth.view.sceneRoots;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;
import modifying.auth.view.tools.AuthToolTip;
import modifying.shared.view.UIUtils;

import java.nio.file.Paths;

public class LoginView extends StackPane {

    private final VBox loginBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private final LoginCtrl loginCtrl;


    public LoginView(LoginCtrl loginCtrl) {
        this.loginCtrl = loginCtrl;

        loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER); // 显式对齐，与 CSS 配合
        initLoginBox();

        getChildren().add(loginBox);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "AuthSceneStyle", "authSceneStyle.css").toUri().toString());

    }

    private void initLoginBox() {
        // 输入框
        usernameField = new TextField();
        usernameField.setPromptText("YOUR NAME");
        passwordField = new PasswordField();
        passwordField.setPromptText("YOUR PASSWORD");

        // 按钮
        Button quitBtn = new Button("Quit");
        Button confirmBtn = new Button("Confirm");
        Button registerBtn = new Button("No account yet? Click here to register");

        quitBtn.setOnAction(event -> loginCtrl.handleLoginQuit());
        quitBtn.setOnMouseEntered(e-> System.out.println("aaaaa"));
        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(
                usernameField.getText(),
                passwordField.getText()
        ));
        registerBtn.setOnAction(e -> loginCtrl.handleRegister());

        // 🔥 注册链接添加专属样式类
        quitBtn.getStyleClass().add("action-button");
        confirmBtn.getStyleClass().add("action-button");
        registerBtn.getStyleClass().add("link-button");

        // 布局
        HBox choiceBox = new HBox(80, quitBtn, confirmBtn);
        Label messageLabel = new Label("Login");

        loginBox.getChildren().addAll(
                messageLabel,
                usernameField,
                passwordField,
                choiceBox,
                registerBtn
        );

    }

    public void clearTextField() {
        usernameField.clear();
        passwordField.clear();
    }
}