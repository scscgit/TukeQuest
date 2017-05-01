package sk.tuke.gamedev.iddqd.tukequest.levels;

import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;
import sk.tuke.gamedev.iddqd.tukequest.util.RandomHelper;

/**
 * Created by Steve on 01.05.2017.
 */
public class BinasExam extends Level {

    public BinasExam() {
        super(
            "Binasova skuska",
            Platform.PlatformTexture.BINARY,
            50,
            Binas.class,
            null,
            VerticalWall.WallTexture.BINARY);
    }

    @Override
    public Background.BackgroundTexture getBackground() {
        return RandomHelper.random.nextBoolean()
            ? Background.BackgroundTexture.ASSEMBLER2
            : Background.BackgroundTexture.ASSEMBLER1;
    }

}
