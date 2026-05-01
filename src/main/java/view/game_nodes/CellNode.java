package view.game_nodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CellNode extends StackPane {
    private final int row;
    private final int col;
    private final int size;
    private int type;

    private ImageView image = null;

    private final static String[] outlook1 = {null, "baidu", "brave", "edge", "firefox", "google",  "ie", "opera", "qq", "quark", "safari", "samsung", "yandex"};

    public CellNode(int row, int col, int size, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.size = size;

        image.setFitWidth(size - 10);
        image.setFitHeight(size - 10);
        image.setPreserveRatio(true);

        setPrefSize(size, size);
        setStyle("-fx-border-color: #888; -fx-border-width: 1px; -fx-background-color: #f0f0f0;");

        updateImage();
        getChildren().addAll(image);
    }

    public void setType(int type) {
        this.type = type;
        updateImage();
    }

    private void updateImage() {
        if (type == 0) {
            image.setImage(null);
        } else if (type < outlook1.length) {
            String path = "/Sprites/Block/Browsers/"+outlook1[type]+".png";
            try {
                Image img = new Image(getClass().getResourceAsStream(path));
                image.setImage(img);
            } catch (Exception e) {
                System.err.println("加载图片失败: " + path);
            }
        }
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            setStyle("-fx-border-color: gold; -fx-border-width: 3px; -fx-background-color: #ffffcc;");
        } else {
            setStyle("-fx-border-color: #888; -fx-border-width: 1px; -fx-background-color: #f0f0f0;");
        }
    }

    //消除动画
    public void playEliminateAnimation(Runnable onFinished) {
        javafx.animation.ScaleTransition st =
                new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), this);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(e -> {
            setVisible(false);
            if (onFinished != null) onFinished.run();
        });
        st.play();
    }
}
