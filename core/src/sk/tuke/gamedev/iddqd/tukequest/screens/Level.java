package sk.tuke.gamedev.iddqd.tukequest.screens;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.assets.PlatformTexture;

/**
 *
 */
public class Level {

    public final String LEVEL_NAME;
    public final PlatformTexture PLATFORM_TEXTURE;
    public final Class TEACHER_CLASS;
    public final String BACKGROUND;
    public final String WALL_TEXTURE;

    public Level(String LEVEL_NAME, PlatformTexture PLATFORM_TEXTURE, Class TEACHER_CLASS, String BACKGROUND, String WALL_TEXTURE) {
        this.PLATFORM_TEXTURE = PLATFORM_TEXTURE;
        this.TEACHER_CLASS = TEACHER_CLASS;
        this.BACKGROUND = BACKGROUND;
        this.WALL_TEXTURE = WALL_TEXTURE;
        this.LEVEL_NAME = LEVEL_NAME;

    }
}
