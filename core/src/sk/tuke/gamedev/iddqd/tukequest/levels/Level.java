package sk.tuke.gamedev.iddqd.tukequest.levels;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;

/**
 *
 */
public class Level {

    public final String levelName;
    public final Platform.PlatformTexture platformTexture;
    public final Class teacherClass;
    protected final Background.BackgroundTexture background;
    public final VerticalWall.WallTexture wallTexture;
    private final int platformCount;
    private int generatedCount;

    public Level(
        String levelName,
        Platform.PlatformTexture platformTexture,
        int platformCount,
        Class teacherClass,
        Background.BackgroundTexture background,
        VerticalWall.WallTexture wallTexture
    ) {
        this.levelName = levelName;
        this.platformTexture = platformTexture;
        this.platformCount = platformCount;
        this.teacherClass = teacherClass;
        this.background = background;
        this.wallTexture = wallTexture;
    }

    public int getPlatformCount() {
        return this.platformCount;
    }

    public void generated() {
        this.generatedCount++;
    }

    public int getGeneratedCount() {
        return this.generatedCount;
    }

    public Background.BackgroundTexture getBackground() {
        return background;
    }

}
