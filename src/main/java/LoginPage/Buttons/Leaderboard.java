package LoginPage.Buttons;

import LoginPage.Account;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Leaderboard {
    public static Button leaderboard(Account account) {
        Button leaderboard = new Button("Leaderboard");
        leaderboard.setPrefSize(400, 50);
        leaderboard.setOnAction(event -> {
            VBox list = new VBox(10);
            for (int i = 1; i <= 30; i++) {
                Label menuItem = new Label("No." + i);
                menuItem.setMaxWidth(Double.MAX_VALUE);
                menuItem.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
                list.getChildren().add(menuItem);
            }

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(list);
            scrollPane.setFitToWidth(true);
            //scrollPane.setPrefSize(300, 300);

            Scene scene = new Scene(scrollPane, 350, 500);
            Stage leaderboardStage = new Stage();
            leaderboardStage.setScene(scene);
            leaderboardStage.show();
        });
        return leaderboard;
    }
}
