package view.boxes;

import controller.LoginCtrl;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegisterBox extends VBox{
    private TextField usernameField;
    private PasswordField passwordField;

    public RegisterBox(LoginCtrl loginCtrl) {
        super(15);

        usernameField = new TextField();
        usernameField.setPromptText("Please enter your username: ");
        usernameField.setMaxWidth(200);

        passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password: ");
        passwordField.setMaxWidth(200);

        Button confirmBtn = new Button("Confirm");
        Button cancelBtn = new Button("Cancel");


        /*confirmBtn.setOnAction(e -> {
            String result = loginCtrl.handleLoginConfirm(usernameField.getText(), passwordField.getText());
            if (result != null) {
                messageLabel.setText(result);
            }
        });

        registerBtn.setOnAction(e -> {
            String result = loginCtrl.handleRegister(usernameField.getText(), passwordField.getText());
            if (result != null) {
                messageLabel.setText(result);
            } else {
                messageLabel.setText("Register succeeded! Please login");
                usernameField.clear();
                passwordField.clear();
            }
        });*/

        getChildren().addAll(usernameField, passwordField, confirmBtn, cancelBtn);
    }
}
