package sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;

import java.util.Arrays;
import java.util.List;

/**
 * Created by macbook on 26/03/2017.
 */
public enum PlatformSize {

    SMALL(96),
    MEDIUM(128),
    LEVEL(TukeQuestGame.SCREEN_WIDTH - (VerticalWall.WALL_WIDTH * 2));

    private int platformWidth;

    PlatformSize(int platformWidth) {
        this.platformWidth = platformWidth;
    }

    public static List<PlatformSize> getAll() {
        return Arrays.asList(SMALL, MEDIUM, LEVEL);
    }

    public int getPlatformWidth() {
        return platformWidth;
    }
}
