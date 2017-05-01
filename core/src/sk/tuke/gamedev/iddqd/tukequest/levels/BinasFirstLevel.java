package sk.tuke.gamedev.iddqd.tukequest.levels;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;

/**
 * Created by Steve on 01.05.2017.
 */
public class BinasFirstLevel extends Level {

    public BinasFirstLevel() {
        super(
            "Binasov level",
            Platform.PlatformTexture.BINARY,
            50,
            Binas.class,
            Background.BackgroundTexture.CODE,
            VerticalWall.WallTexture.BINARY);
    }

}
