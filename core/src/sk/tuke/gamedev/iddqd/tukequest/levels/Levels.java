package sk.tuke.gamedev.iddqd.tukequest.levels;

/**
 * Created by Steve on 01.05.2017.
 */
public enum Levels {

    BINAS(new BinasLevel()),
    GENCI(new GenciLevel()),
    PORUBAN(new PorubanLevel());

    public final Level level;

    Levels(Level level) {
        this.level = level;
    }

}
