package modifying.shared.resources;

import com.google.gson.Gson;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {

    private final Image atlasImage;
    private final Map<String, Rectangle2D> regionMap = new HashMap<>();

    public TextureAtlas(String imagePath, String jsonPath) {
        // 1. 加载图集大图
        atlasImage = new Image(getClass().getResourceAsStream(imagePath));

        // 2. 解析 JSON
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream(jsonPath))) {
            Gson gson = new Gson();
            // 🔥 修正：使用 AtlasData 而不是 AtlasRoot
            AtlasData root = gson.fromJson(reader, AtlasData.class);

            for (FrameData frame : root.frames()) {
                FrameRect rect = frame.frame();
                Rectangle2D region = new Rectangle2D(
                        rect.x(), rect.y(), rect.w(), rect.h()
                );
                regionMap.put(frame.filename(), region);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load atlas: " + jsonPath, e);
        }
    }

    public Image getAtlasImage() {
        return atlasImage;
    }

    public Rectangle2D getRegion(String filename) {
        return regionMap.get(filename);
    }

    public ImageView createImageView(String filename) {
        Rectangle2D region = getRegion(filename);
        if (region == null) {
            throw new IllegalArgumentException("Unknown sprite: " + filename);
        }
        ImageView view = new ImageView(atlasImage);
        view.setViewport(region);
        return view;
    }
}