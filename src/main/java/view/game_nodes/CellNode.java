package view.game_nodes;

import controller.GameCtrl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.entity.Crd;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class CellNode extends StackPane {
    private final Crd crd;
    private final int type;
    private final int imgSet;
    private final ImageView image;
    private static HashMap<Integer, String> imgSets = new HashMap<>();
    private boolean isBomb = false;

    private final static String[][] images = {
            {"baidu", "brave", "edge", "firefox", "google", "ie", "opera", "qq", "quark", "safari", "samsung", "yandex"},
            {"Enchanted Book", "Fishing Rod", "Leather Boots", "Name Tag", "Nautilus Shell", "Pufferfish", "Raw Cod", "Raw Salmon", "Shaddle", "Suspicious Stew", "Tropical Fish", "Water Bottle"}
    };

    public int getType() {
        return type;
    }

    public Crd getCrd() {
        return crd;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
        setHighlight(bomb);
        updateImage();
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            getStyleClass().add("highlight");
        } else {
            getStyleClass().remove("highlight");
        }
    }

    public static void initHashSet() {
        imgSets.put(0, "Browsers");
        imgSets.put(1, "MCFishing");
    }

    public CellNode(int row, int col, double size, int type, GameCtrl gameCtrl, int imgSet) {
        initHashSet();

        crd = new Crd(row, col);
        this.type = type;
        this.imgSet = imgSet;

        getStylesheets().add(getClass().getResource("/css/cellNode.css").toExternalForm());

        setPrefSize(size, size);
        setOnMouseClicked(event -> gameCtrl.handleCellClick(this));
        image = new ImageView();
        updateImage();
        image.setFitWidth(size);
        image.setFitHeight(size);
        getChildren().add(image);
    }

    private void updateImage() {
        Path directorpath;
        if (type == -1) {
            image.setImage(null);
        } else if (type < images[imgSet].length) {
            if(isBomb) {
                directorpath = Paths.get("src", "main", "resources", "Sprites", "Block", "TNT" + ".png");
            } else {
                directorpath = Paths.get("src", "main", "resources", "Sprites", "Block", imgSets.get(imgSet), images[imgSet][type] + ".png");
            }
            try (InputStream is = Files.newInputStream(directorpath)) {
                Image img = new Image(is);
                image.setImage(img);
            } catch (Exception e) {
                System.err.println("加载图片失败: " + directorpath);
            }
        }
    }

    public void eliminateCell() {
        javafx.animation.ScaleTransition st =
                new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), this);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(e -> setVisible(false));
        st.play();
    }
}
