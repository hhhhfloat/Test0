package view.game_nodes;

import controller.GameCtrl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.entity.Crd;

public class CellNode extends StackPane {
    private final GameCtrl gameCtrl;
    private final Crd crd;
    private int type;

    private ImageView image;

    private final static String[] outlook1 = {"baidu", "brave", "edge", "firefox", "google", "ie", "opera", "qq", "quark", "safari", "samsung", "yandex"};

    public CellNode(int row, int col, double size, int type, GameCtrl gameCtrl) {
        crd = new Crd(row, col);
        this.type = type;
        this.gameCtrl = gameCtrl;
        setPrefSize(size, size);
        setLayoutX(row * (size + 10));
        setLayoutY(col * (size + 10));
        setOnMouseClicked(event -> gameCtrl.handleCellClick(this));

        image = new ImageView();
        updateImage();
        image.setFitWidth(size);
        image.setFitHeight(size);
        getChildren().addAll(image);
    }

    public Crd getCrd() {
        return crd;
    }

    public void setType(int type) {
        this.type = type;
        updateImage();
    }

    private void updateImage() {
        if (type == -1) {
            image.setImage(null);
        } else if (type < outlook1.length) {
            String path = "/Sprites/Block/Browsers/" + outlook1[type] + ".png";
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

    public void eliminate() {
        javafx.animation.ScaleTransition st =
                new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), this);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(e -> setVisible(false));
        st.play();
    }
}
