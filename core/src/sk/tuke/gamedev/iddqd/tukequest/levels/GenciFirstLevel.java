package sk.tuke.gamedev.iddqd.tukequest.levels;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Genci;

/**
 * Created by Steve on 01.05.2017.
 */
public class GenciFirstLevel extends Level {

    public GenciFirstLevel() {
        super(
            "Genciho level",
            Platform.PlatformTexture.MATHS,
            50,
            Genci.class,
            Background.BackgroundTexture.ICY_TOWER,
            VerticalWall.WallTexture.BINARY);
    }

}
