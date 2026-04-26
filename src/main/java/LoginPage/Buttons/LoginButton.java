package LoginPage.Buttons;

import LoginPage.Account;
import LoginPage.AccountScene;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class LoginButton {
    public static Button login(Stage stage, Account account) {
        Map<String, String> users = account.getMap();

        Stage loginStage = new Stage();
        loginStage.setTitle("Account Login");

        VBox loginBox = new VBox(), registerBox = new VBox();
        Scene loginScene = new Scene(loginBox,400,150), registerScene = new Scene(registerBox,400,150);

        Button registerButton = new Button("Don't have an account yet?Click here to register");
        registerButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 12px;" +
                        "-fx-underline: true;"
        );
        registerButton.setOnMouseEntered(e ->
                registerButton.setStyle(registerButton.getStyle() + "-fx-text-fill: #0066cc;")
        );
        registerButton.setOnMouseExited(e ->
                registerButton.setStyle(registerButton.getStyle().replace("-fx-text-fill: #0066cc;", "-fx-text-fill: #333333;"))
        );
        registerButton.setOnAction(event -> {
            TextField accountIn = new TextField();
            TextField passwordIn = new PasswordField();
            accountIn.setPromptText("Please enter your account:");
            accountIn.setPrefWidth(200);
            passwordIn.setPromptText("Please enter your password:");
            passwordIn.setPrefWidth(200);
            String s1 = accountIn.getText(), s2 = passwordIn.getText();
            Button confirm = new Button("Register and login");
            confirm.setOnAction(actionEvent1 -> {
                try {
                    FileWriter fileWriter = new FileWriter("src/main/userdata/users.txt", true);
                    if (s1.trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Username can't be null!");
                        alert.showAndWait();
                    } else if (users.containsKey(s1)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Username's already used!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Register succeeded!Login");
                        alert.showAndWait();
                        fileWriter.write(s1 + "&" + s2 + "\n");
                        fileWriter.close();
                        loginStage.close();
                        stage.setScene(AccountScene.getAccountScene(stage, account, false));
                    }
                } catch (IOException e) {
                }
            });

            Button cancel = new Button("Cancel");
            cancel.setOnAction(actionEvent1 -> loginStage.setScene(loginScene));

            HBox hBox = new HBox();
            hBox.getChildren().addAll(confirm, cancel);
            if (registerBox.getChildren().isEmpty()) {
                registerBox.getChildren().addAll(accountIn, passwordIn, hBox);
            }
            loginStage.setScene(registerScene);
        });

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(400, 50);
        loginButton.setOnAction(actionEvent -> {
            TextField accountIn = new TextField();
            TextField passwordIn = new PasswordField();
            accountIn.setPromptText("Please enter your account:");
            accountIn.setPrefWidth(200);
            passwordIn.setPromptText("Please enter your password:");
            passwordIn.setPrefWidth(200);
            String s1 = accountIn.getText(), s2 = passwordIn.getText();
            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            HBox hBox = new HBox();
            hBox.getChildren().addAll(confirm, cancel);

            if (loginBox.getChildren().isEmpty()) {
                loginBox.getChildren().addAll(accountIn, passwordIn, hBox, registerButton);
            }

            loginStage.setScene(loginScene);
            loginStage.show();

            confirm.setOnAction(actionEvent1 -> {
                if (s1.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Username can't be null!");
                    alert.showAndWait();
                } else if (!users.containsKey(s1)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Username doesn't exist!");
                    alert.showAndWait();
                } else {
                    if (s2.equals(users.get(s1))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Login succeeded!");
                        alert.showAndWait();
                        account.setAccountName(s1);
                        loginStage.close();
                        stage.setScene(AccountScene.getAccountScene(stage, account, false));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("Password is incorrect!");
                        alert.showAndWait();
                    }
                }
            });
            cancel.setOnAction(actionEvent1 -> loginStage.close());
        });
        return loginButton;
    }
}
