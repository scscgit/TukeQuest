package sk.tuke.gamedev.iddqd.tukequest.actors.game.assets;

/**
 *
 */
public enum PlatformTexture {

    MATHS("maths_texture.jpg"),
    ROCK("jerusrockwallsml.jpg"),
    CHIMNEY("cabin_chimney_side.jpg");

    private String textureFileName;

    PlatformTexture(String s) {
        this.textureFileName = s;
    }

    public String getTextureFileName() {
        return textureFileName;
    }
}
