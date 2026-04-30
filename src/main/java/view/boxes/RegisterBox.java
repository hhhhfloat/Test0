package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterBox extends VBox{
    private final TextField usernameField;
    private final PasswordField passwordField;

    public RegisterBox(LoginCtrl loginCtrl) {
        super(15);

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        usernameField.setMaxWidth(200);

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");
        passwordField.setMaxWidth(200);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> loginCtrl.handleRegisterCancel());
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(event -> loginCtrl.handleRegisterConfirm(usernameField.getText(), passwordField.getText()));
        HBox hBox = new HBox(80, cancelBtn, confirmBtn);

        Label messageLabel;
        messageLabel = new Label("Register Your Account");

        getChildren().addAll(messageLabel, usernameField, passwordField, hBox);
    }
}
