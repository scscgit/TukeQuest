package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.PlatformSize;
import sk.tuke.gamedev.iddqd.tukequest.util.RandomHelper;

import java.util.ArrayList;
import java.util.List;

// TODO: integrate to the PlatformManager; don't use static fields, as this breaks when scene changes
public class PlatformGenerator {

    private static final int PLATFORM_TEXTURE_CHANGE_RATE = 50;
    public static final int Y_DISTANCE_BETWEEN_PLATFORMS = 64;

    public static float highestPlatformY;

    private static int platformCount;
    private static int currentTextureIndex;

    public static void reset() {
        PlatformGenerator.platformCount = 0;
        PlatformGenerator.currentTextureIndex = 0;
    }

    private static List<Platform> generateNext(float startingY, Platform.PlatformTexture texture) {
        return generateNext(PLATFORM_TEXTURE_CHANGE_RATE, startingY, texture);
    }

    // TODO: maybe implement some LEVEL algorithm that will increase the difficulty given the value of Y
    private static List<Platform> generateNext(int count, float startingY, Platform.PlatformTexture texture) {
        int PLATFORM_WIDTH = 128;
        int X_COORDINATE_RANGE = TukeQuestGame.SCREEN_WIDTH - PLATFORM_WIDTH - 2 * VerticalWall.WALL_WIDTH;

        List<Platform> platforms = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            //platformCount++;
            startingY = startingY + Y_DISTANCE_BETWEEN_PLATFORMS;

            if (i == 0) {
                changePlatformType();
                Platform levelPlatform = new Platform(VerticalWall.WALL_WIDTH + 1, startingY, PlatformSize.LEVEL, texture);
                platforms.add(levelPlatform);


                // FIXME: 30/04/2017 decouple logic
                TeacherGenerator.levelPlatforms.add(levelPlatform);
            } else {
                int randomStartingX =
                    RandomHelper.random.nextInt((X_COORDINATE_RANGE - VerticalWall.WALL_WIDTH) + 1)
                        + VerticalWall.WALL_WIDTH;
                platforms.add(createSmallOrMediumPlatform(randomStartingX, startingY, texture));
            }
        }

        highestPlatformY = startingY;

        return platforms;
    }

    private static Platform createSmallOrMediumPlatform(int randomStartingX, float startingY, Platform.PlatformTexture texture) {
        PlatformSize size;
        if (RandomHelper.random.nextBoolean()) {
            size = PlatformSize.SMALL;
        } else {
            size = PlatformSize.MEDIUM;
        }

        return new Platform(randomStartingX, startingY, size, texture);
    }

    private static void changePlatformType() {
        System.out.println("Should change platform type now!!!");
        int texturesCount = Platform.getPlatformTexturesCount();
        int textureOrder = platformCount / PLATFORM_TEXTURE_CHANGE_RATE;

        // this is to make sure there is always a texture
        // this method allows for cyclic reuse of textures from first to last and over again
        currentTextureIndex = textureOrder % texturesCount;
    }

    public static List<Platform> generateNext(Platform.PlatformTexture texture) {
        return generateNext(highestPlatformY, texture);
    }

}
