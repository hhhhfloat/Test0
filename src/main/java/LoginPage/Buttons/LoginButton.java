package LoginPage.Buttons;

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
    Map<String, String> users;

    public LoginButton(Map<String, String> users) {
        this.users = users;
    }

    public Button login(final Stage stage) {
        class Register {
            static Button register(Map<String, String> users) {
                TextField account = new TextField();
                TextField password = new PasswordField();
                account.setPromptText("Please enter your account:");
                account.setPrefWidth(200);
                password.setPromptText("Please enter your password:");

                Stage registerStage = new Stage();

                Button registerButton = new Button("Don't have an account yet?Click here to register");
                registerButton.setOnAction(actionEvent -> {
                    Button confirm = new Button("Confirm");
                    confirm.setOnAction(actionEvent1 -> {
                        try {
                            String s1 = account.getText(), s2 = password.getText();
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
                                alert.setContentText("Register succeeded!");
                                alert.showAndWait();
                                fileWriter.write(s1 + "&" + s2 + "\n");
                                fileWriter.close();
                                registerStage.close();
                            }
                        } catch (IOException e) {
                        }
                    });

                    Button cancel = new Button("Cancel");
                    cancel.setOnAction(actionEvent1 -> registerStage.close());

                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(confirm, cancel);
                    VBox registerBox = new VBox();
                    registerBox.getChildren().addAll(account, password,hBox);
                    registerStage.setTitle("Account Register");
                    registerStage.setScene(new Scene(registerBox));
                    registerStage.show();
                });
                return registerButton;
            }
        }
        TextField account = new TextField();
        TextField password = new PasswordField();
        account.setPromptText("Please enter your account:");
        account.setPrefWidth(200);
        password.setPromptText("Please enter your password:");

        Button loginButton = new Button("Login");
        Button registerButton = Register.register(users);
        loginButton.setPrefSize(400, 50);
        loginButton.setOnAction(actionEvent -> {
            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            HBox hBox = new HBox();
            hBox.getChildren().addAll(confirm, cancel);
            VBox loginBox = new VBox();
            loginBox.getChildren().addAll(account, password, hBox, registerButton);

            Stage loginStage = new Stage();
            loginStage.setTitle("Account Login");
            loginStage.setScene(new Scene(loginBox));
            loginStage.show();

            confirm.setOnAction(actionEvent1 -> {
                String s1 = account.getText(), s2 = password.getText();
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
                        loginStage.close();
                        stage.setScene(AccountScene.getAccountScene(stage));
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
