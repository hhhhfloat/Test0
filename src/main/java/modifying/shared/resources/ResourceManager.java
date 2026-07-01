package modifying.shared.resources;

public class ResourceManager {

    private static final ResourceManager instance = new ResourceManager();

    private final TextureAtlas authAtlas;
    private final TextureAtlas gameAtlas;

    private ResourceManager() {
        // 加载认证模块的图集
        authAtlas = new TextureAtlas(
                "/Sprites/sprites/authSpriteProperty.png",
                "/Sprites/json/authSpriteProperty.json"
        );
        gameAtlas = new TextureAtlas(
                "/Sprites/sprites/gameSpriteProperty.png",
                "/Sprites/json/gameSpriteProperty.json"
        );
    }

    public static ResourceManager getInstance() {
        return instance;
    }

    public TextureAtlas getAuthAtlas() {
        return authAtlas;
    }
    public TextureAtlas getGameAtlas(){return gameAtlas;}
}