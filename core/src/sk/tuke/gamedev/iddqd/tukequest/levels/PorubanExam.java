package sk.tuke.gamedev.iddqd.tukequest.levels;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;

/**
 * Created by Steve on 01.05.2017.
 */
public class PorubanExam extends Level {

    public PorubanExam() {
        super(
            "Porubanova skuska",
            Platform.PlatformTexture.CHIMNEY,
            50,
            Poruban.class,
            Background.BackgroundTexture.ICY_TOWER,
            VerticalWall.WallTexture.BINARY);
    }

}
