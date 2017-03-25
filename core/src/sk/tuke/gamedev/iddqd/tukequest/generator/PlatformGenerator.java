package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by macbook on 25/03/2017.
 */
public class PlatformGenerator {
    private  static final int Y_STEP = 70;

    // TODO: maybe implement some LEVEL algorithm that will increase the difficulty given the value of Y
    public static List<Platform> generateNext(int startingY) {
        int WALL_WIDTH = 40;
        int PLATFORM_WIDTH = 128;
        int X_COORDINATE_RANGE = TukeQuestGame.SCREEN_WIDTH - PLATFORM_WIDTH - 2* WALL_WIDTH;

        List<Platform> platforms = new ArrayList<>();

        Random random = new Random();
        for (int i=0; i<10; i++) {
            startingY  = startingY + Y_STEP;
            int randomNum = random.nextInt((X_COORDINATE_RANGE - WALL_WIDTH) + 1) + WALL_WIDTH;
            platforms.add(new Platform(randomNum, startingY));
        }

        return platforms;
    }
}
