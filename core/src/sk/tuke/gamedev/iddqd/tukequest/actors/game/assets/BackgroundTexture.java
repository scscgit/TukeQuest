package sk.tuke.gamedev.iddqd.tukequest.actors.game.assets;

import java.util.List;

/**
 *
 */
public enum BackgroundTexture {
    ICY_TOWER("background.jpg");

    private String textureFileName;

    BackgroundTexture(String s) {
        this.textureFileName = s;
    }

    public String getTextureFileName() {
        return textureFileName;
    }
}
