package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {

    private static final int PLATFORM_TEXTURE_CHANGE_RATE = 50;
    private static final int Y_STEP = 64;

    public static float highestPlatformY;

    private static int platformCount;
    private static int currentTextureIndex;

    // TODO: maybe implement some LEVEL algorithm that will increase the difficulty given the value of Y
    public static List<Platform> generateNext(int count, float startingY) {
        int WALL_WIDTH = 40;
        int PLATFORM_WIDTH = 128;
        int X_COORDINATE_RANGE = TukeQuestGame.SCREEN_WIDTH - PLATFORM_WIDTH - 2 * WALL_WIDTH;

        List<Platform> platforms = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < count; i++) {
            platformCount++;
            startingY = startingY + Y_STEP;
            int randomNum = random.nextInt((X_COORDINATE_RANGE - WALL_WIDTH) + 1) + WALL_WIDTH;
            platforms.add(new Platform(randomNum, startingY, currentTextureIndex));

            if (platformCount % PLATFORM_TEXTURE_CHANGE_RATE == 0) {
                changePlatformType();
            }
        }

        highestPlatformY = startingY;

        return platforms;
    }

    private static void changePlatformType() {
        System.out.println("Should change platform type now!!!");
        int texturesCount = Platform.ANIMATIONS.size();
        int textureOrder = platformCount / PLATFORM_TEXTURE_CHANGE_RATE;

        // this is to make sure there is always a texture
        // this method allows for cyclic reuse of textures from first to last and over again
        currentTextureIndex = textureOrder % texturesCount;
    }

    public static List<Platform> generateNext(int count) {
        return generateNext(count, highestPlatformY);
    }

}
