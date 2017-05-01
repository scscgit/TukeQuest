package sk.tuke.gamedev.iddqd.tukequest.screens;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;

/**
 *
 */
public class Level {

    public final String LEVEL_NAME;
    public final Platform.PlatformTexture PLATFORM_TEXTURE;
    public final Class TEACHER_CLASS;
    public final Background.BackgroundTexture BACKGROUND;
    public final VerticalWall.WallTexture WALL_TEXTURE;

    public Level(String LEVEL_NAME, Platform.PlatformTexture PLATFORM_TEXTURE, Class TEACHER_CLASS, Background.BackgroundTexture BACKGROUND, VerticalWall.WallTexture WALL_TEXTURE) {
        this.PLATFORM_TEXTURE = PLATFORM_TEXTURE;
        this.TEACHER_CLASS = TEACHER_CLASS;
        this.BACKGROUND = BACKGROUND;
        this.WALL_TEXTURE = WALL_TEXTURE;
        this.LEVEL_NAME = LEVEL_NAME;

    }
}
