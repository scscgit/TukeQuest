package sk.tuke.gamedev.iddqd.tukequest.levels;

/**
 * Created by Steve on 01.05.2017.
 */
public enum Levels {

    BINAS(new BinasFirstLevel()),
    BINAS_EXAM(new BinasExam()),
    GENCI(new GenciFirstLevel()),
    GENCI_EXAM(new GenciExam()),
    PORUBAN(new PorubanFirstLevel()),
    PORUBAN_EXAM(new PorubanExam());

    public final Level level;

    Levels(Level level) {
        this.level = level;
    }

}
