package modifying.auth.view.sceneRoots;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modifying.auth.controller.LoginCtrl;

import java.nio.file.Paths;

public class RegisterView extends StackPane {

    private final VBox registerBox;
    private final LoginCtrl loginCtrl;

    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmField;

    public RegisterView(LoginCtrl loginCtrl) {
        this.loginCtrl = loginCtrl;

        registerBox = new VBox(15);
        registerBox.setAlignment(Pos.CENTER);
        initRegisterBox();

        getChildren().add(registerBox);
        // 🔥 使用统一的 CSS 文件
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "AuthSceneStyle", "authSceneStyle.css").toUri().toString());
    }

    private void initRegisterBox() {
        // 标题
        Label messageLabel = new Label("Register");

        // 输入框
        usernameField = new TextField();
        usernameField.setPromptText("YOUR NAME");

        passwordField = new PasswordField();
        passwordField.setPromptText("YOUR PASSWORD");

        confirmField = new PasswordField();
        confirmField.setPromptText("CONFIRM PASSWORD");

        // 输入限制（2000 字符）
        usernameField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 2000) return null;
            return change;
        }));
        passwordField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 2000) return null;
            return change;
        }));
        confirmField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 2000) return null;
            return change;
        }));

        // 按钮
        Button cancelBtn = new Button("Cancel");
        Button confirmBtn = new Button("Confirm");
        cancelBtn.getStyleClass().add("action-button");
        confirmBtn.getStyleClass().add("action-button");

        cancelBtn.setOnAction(event -> loginCtrl.handleRegisterCancel());
        confirmBtn.setOnAction(event -> loginCtrl.handleRegisterConfirm(
                usernameField.getText(),
                passwordField.getText(),
                confirmField.getText()
        ));

        HBox choiceBox = new HBox(80, cancelBtn, confirmBtn);

        registerBox.getChildren().addAll(
                messageLabel,
                usernameField,
                passwordField,
                confirmField,
                choiceBox
        );
    }

    public void clearTextField() {
        usernameField.clear();
        passwordField.clear();
        confirmField.clear();
    }
}