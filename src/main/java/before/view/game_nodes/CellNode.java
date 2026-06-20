package before.view.game_nodes;

import before.controller.GameCtrl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import before.model.entity.Crd;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class CellNode extends StackPane {
    private final Crd crd;
    private final int type;
    private final int imgSet;
    private final ImageView image;
    private boolean isBomb = false;
    private String name;
    private String[] usingImgSet;
    private String usingImgSetName;
    private int isPair;

    private final static String[] imageSetNames_notPair = {
            "Browsers","MCFishing","AI","Chats"
    };
    private final static String[] imageSetNames_Pair ={
            "antipair","IndieGame"
    };
    private final static String[][] images_notPair = {
            {"baidu", "brave", "edge", "firefox", "google", "ie", "opera", "qq", "quark", "safari", "samsung", "yandex"},
            {"Enchanted Book", "Fishing Rod", "Leather Boots", "Name Tag", "Nautilus Shell", "Pufferfish", "Raw Cod", "Raw Salmon", "Shaddle", "Suspicious Stew", "Tropical Fish", "Water Bottle"},
            {"ChatGPT","Claude","DeepSeek","Doubao","Gemini","GithubCopilot","Kimi","Midjourney","Perplexity","Qianwen","stability","wenxinyiyan"},
            {"discord","facebook","myspace","qq","reddit","twitter","wechat","wecom","weibo","whatapp","x","yahoo"}
    };
    private final static String[][] images_pair = {
            {"00cangqiong","01chiri","02dalu","03changkong","04han","05he","06hua","07song","08rixia","09tianzhong","10haishu","11shanhua","12di","13tian","14tiangong","15yuedian","16feng","17yu","18leigong","19yubo","20shicui","21tihong","22xuedong","23yanlou"},
            {"00ceilingfan","01horrortie","02baba","03keke","04lightbulb","05neko","06ship60","07watch","08determination","09tobyfox","10ida","11totem","12charactor1","13character2","14cuphead","15handgun","16doublejump","17dreamnail","18cherry","19feather","20ancientfruit","21purplepants","22min","23toastedmarshmallow"}
    };
    private static final int[] imageSetTotNum = {4,2};

    public static int getImageTotNum(int isPair){
        return imageSetTotNum[isPair-1];
    }

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

    public String getName() {
        return name;
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            getStyleClass().add("highlight");
        } else {
            getStyleClass().remove("highlight");
        }
    }

    public CellNode(int row, int col, double size, int type, GameCtrl gameCtrl, int imgSet, int isPair) {
        usingImgSet = (isPair == 2)?images_pair[imgSet] : images_notPair[imgSet];
        usingImgSetName = (isPair == 2)?imageSetNames_Pair[imgSet]:imageSetNames_notPair[imgSet];
        crd = new Crd(row, col);
        this.type = type;
        this.imgSet = imgSet;
        if(type>=0){
            this.name = (isPair == 1)?images_notPair[imgSet][type]:images_pair[imgSet][type];
        }

        Optional.ofNullable(getClass().getResource("/css/cellNode.css"))
                .map(URL::toExternalForm)
                .ifPresent(url -> getStylesheets().add(url));

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
        if (type < 0) {
            image.setImage(null);
        } else if (type < ((isPair == 1)?images_notPair[0].length:images_pair[0].length)) {
            if(isBomb) {
                directorpath = Paths.get("src", "main", "resources", "Sprites", "Block", "TNT" + ".png");
            } else {
                directorpath = Paths.get("src", "main", "resources", "Sprites", "Block", usingImgSetName, usingImgSet[type] + ".png");
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

    public void setHint() {
        final String originalStyle = getStyle();
        final double originalTx = getTranslateX();
        final double originalTy = getTranslateY();
        Timeline flicker = new Timeline(
                new KeyFrame(Duration.seconds(0.3), event -> {
                    setTranslateX(-3);
                    setTranslateY(-3);
                    setStyle("-fx-border-width: 3px; -fx-border-color: red;");
                }),
                new KeyFrame(Duration.seconds(0.4), event -> setStyle("-fx-border-width: 3px; -fx-border-color: transparent;"))
        );
        flicker.setCycleCount(2);
        flicker.play();

        setStyle("-fx-border-width: 0px; -fx-border-color: transparent;");
        setTranslateX(3);
        setTranslateY(3);

        flicker.setOnFinished(e->{
            setStyle(originalStyle);
            setTranslateX(originalTx);
            setTranslateY(originalTy);
        });
    }
}
