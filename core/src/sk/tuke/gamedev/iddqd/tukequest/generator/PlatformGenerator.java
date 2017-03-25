package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {

    private static int platformCount = 0;

    private  static final int Y_STEP = 64;
    public static int highestPlatformY;

    // TODO: maybe implement some LEVEL algorithm that will increase the difficulty given the value of Y
    public static List<Platform> generateNext(int count, int startingY) {
        int WALL_WIDTH = 40;
        int PLATFORM_WIDTH = 128;
        int X_COORDINATE_RANGE = TukeQuestGame.SCREEN_WIDTH - PLATFORM_WIDTH - 2* WALL_WIDTH;

        List<Platform> platforms = new ArrayList<>();

        Random random = new Random();
        for (int i=0; i<count; i++) {
            startingY  = startingY + Y_STEP;
            int randomNum = random.nextInt((X_COORDINATE_RANGE - WALL_WIDTH) + 1) + WALL_WIDTH;
            platforms.add(new Platform(randomNum, startingY));

            if (platformCount % 50 == 0) {
                changePlatformType();
            }
        }

        platformCount = platformCount + count;
        highestPlatformY = startingY;

        return platforms;
    }

    private static void changePlatformType() {
        System.out.println("Should change platform type now!!!");
    }

    public static List<Platform> generateNext(int count) {
        return generateNext(count, highestPlatformY);
    }
}
