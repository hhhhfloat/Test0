package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginBox extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginBox(LoginCtrl loginCtrl) {
        super(15);

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");
        Button registerBtn = new Button("Don't have an account yet? Click here to register");

        HBox hBox = new HBox(80, cancelBtn, confirmBtn);

        messageLabel = new Label();

        cancelBtn.setOnAction(event -> loginCtrl.showInitialScene());
        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText()));
        registerBtn.setOnAction(e -> loginCtrl.handleRegister(usernameField.getText(), passwordField.getText()));

        getChildren().addAll(usernameField, passwordField, hBox, registerBtn, messageLabel);
    }
}
