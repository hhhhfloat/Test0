package before.view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import modifying.auth.model.Account;
import modifying.auth.model.ScoreEntry;
import modifying.auth.view.tools.MouseGlowEffect;

import java.nio.file.Paths;
import java.util.List;

public class LeaderboardScene extends Scene {
    private static StackPane leaderboard;
    private static Account account;
    public LeaderboardScene(List<ScoreEntry> userList, Account account){
        super(new StackPane(createRoot(userList, account)), 600,600);
        getStylesheets().add(Paths.get("src", "main", "resources", "css", "leaderboard.css").toUri().toString());
        leaderboard = (StackPane) getRoot();
        MouseGlowEffect.attach(this, leaderboard);
    }

    private static StackPane createRoot(List<ScoreEntry> userList, Account account){
        LeaderboardScene.account = account;

        VBox listBox = MakeLabelList(userList);
        ScrollPane scrollPane = new ScrollPane(listBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(460);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 分隔线（使用 Region 模拟）
        Region separator = new Region();
        separator.getStyleClass().add("separator-line");
        separator.setMaxWidth(440);

        // 底部当前用户信息
        Label currentUserLabel = new Label((account == null) ? "TOURIST MODE" : userInfo);
        currentUserLabel.getStyleClass().addAll("label", "current-user");
        currentUserLabel.setWrapText(true);
        currentUserLabel.setMaxWidth(440);

        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(scrollPane, separator, currentUserLabel);

        StackPane root = new StackPane(content);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }
    private static String userInfo = "";
    private static VBox MakeLabelList(List<ScoreEntry> userList){
        VBox list = new VBox(10);
        list.setAlignment(Pos.TOP_CENTER);
        for (int i = 0; i < userList.size(); i++) {
            ScoreEntry entry = userList.get(i);

            // 底层empty label
            Label bottomStyle = new Label();
            bottomStyle.getStyleClass().add("label");
            bottomStyle.setMaxWidth(Double.MAX_VALUE);

            // 上层：排名+用户名+Score，透明背景
            String rank = String.format("%d",i+1);
            String name = cutName(entry.getName(), 10);
            String score = String.format("SCORE: %d",entry.getScore());

            Label rankLayer = new Label(rank);
            rankLayer.getStyleClass().add("rank");

            Label userLayer = new Label(name);
            userLayer.getStyleClass().add("name");

            Label scoreLayer = new Label(score);
            scoreLayer.getStyleClass().add("score");

            HBox infos = new HBox(10);  // 间距10px
            infos.setAlignment(Pos.CENTER_LEFT);
            infos.setMaxWidth(Double.MAX_VALUE);
            infos.getChildren().addAll(rankLayer, userLayer, scoreLayer);
// 让 HBox 填满父容器宽度
            HBox.setHgrow(rankLayer, Priority.NEVER);
            HBox.setHgrow(userLayer, Priority.ALWAYS);   // 用户名区域可拉伸，占据中间空间
            HBox.setHgrow(scoreLayer, Priority.NEVER);
            infos.setPadding(new Insets(0, 25, 0, 25));


            StackPane entryPane = new StackPane(bottomStyle, infos);
            entryPane.setMaxWidth(400);

            if(account != null && account.getUserName().equals(entry.getName())) {
                userInfo = rank + " | Score: " + entry.getScore();
            }
            list.getChildren().add(entryPane);
        }
        return list;
    }
    public static String cutName(String username, int maxLenght){
        if(username==null)return "";
        return username.length()<=maxLenght?username:username.substring(0,maxLenght)+"...";
    }

}
