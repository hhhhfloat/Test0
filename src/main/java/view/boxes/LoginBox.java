package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginBox extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginBox(LoginCtrl loginCtrl) {
        super(15);
        //setAlignment(Pos.CENTER);
        //setStyle("-fx-padding: 20; -fx-background-color: rgba(0,0,0,0.7); -fx-background-radius: 10;");

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        usernameField.setMaxWidth(200);

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");
        passwordField.setMaxWidth(200);

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");
        Button registerBtn = new Button("Register");

        messageLabel = new Label();
        //messageLabel.setStyle("-fx-text-fill: red;");

        cancelBtn.setOnAction(event -> loginCtrl.showInitialScene());

        confirmBtn.setOnAction(e -> loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText()));

        registerBtn.setOnAction(e -> loginCtrl.handleRegister(usernameField.getText(), passwordField.getText()));

        getChildren().addAll(usernameField, passwordField, confirmBtn, cancelBtn, registerBtn, messageLabel);
    }
}
