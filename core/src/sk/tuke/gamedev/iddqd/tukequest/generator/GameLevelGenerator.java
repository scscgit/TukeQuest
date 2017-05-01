package sk.tuke.gamedev.iddqd.tukequest.generator;

import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Collectable;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameLevelGenerator {

    private final GameScreen screen;
    private final Level level;
    private float startingY;
    private float levelEndY;

    public GameLevelGenerator(GameScreen screen, Level level, float startingY) {
        this.screen = screen;
        this.level = level;
        this.startingY = startingY;
    }

    public List<Actor> generateLevel() {
        System.out.println("Generating LEVEL = " + level.levelName);
        List<Actor> generatedActors = new ArrayList<>();
        levelEndY = startingY + level.getPlatformCount() * PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS;

        generatedActors.addAll(generateBackgrounds());
        List<Platform> levelPlatforms = PlatformGenerator.generateNext(
            level.getPlatformCount(),
            // Generate platforms starting from height of the ground
            this.startingY,
            level.platformTexture);
        generatedActors.addAll(levelPlatforms);
        Platform firstPlatform = levelPlatforms.get(0);

        for (int i = 1; i < levelPlatforms.size(); i++) {
            Platform platform = levelPlatforms.get(i);
            Collectable collectable = CollectableGenerator.createForPlatform(platform);
            if (collectable != null) {
                generatedActors.add(collectable);
            }
        }

        // Generate teacher on first platform (this should be the BIG one)
        generatedActors.add(TeacherGenerator.generateTeacherOnPlatform(
            this.level.teacherClass,
            firstPlatform,
            screen));

        // Generate walls at the sides
        generatedActors.addAll(generateWalls());

        this.level.generated();
        return generatedActors;
    }


    // Generate walls for whole level
    private List<VerticalWall> generateWalls() {
        List<VerticalWall> generatedWalls = new ArrayList<>();
        float wallsMaxY = startingY;
        while (wallsMaxY < this.levelEndY) {
            VerticalWall leftWall = new VerticalWall(VerticalWall.Side.LEFT, wallsMaxY, level.wallTexture);
            VerticalWall rightWall = new VerticalWall(VerticalWall.Side.RIGHT, wallsMaxY, level.wallTexture);
            generatedWalls.add(leftWall);
            generatedWalls.add(rightWall);
            wallsMaxY = wallsMaxY + leftWall.getHeight();
            System.out.println("New wallsMaxY " + wallsMaxY);
        }
        return generatedWalls;
    }

    private List<Background> generateBackgrounds() {
        List<Background> generatedBackgrounds = new ArrayList<>();
        float backgroundMaxY = this.startingY + PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS;
        while (backgroundMaxY < this.levelEndY + PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS) {
            Background background = new Background(backgroundMaxY, level.getBackground());
            generatedBackgrounds.add(background);
            backgroundMaxY += background.getAnimation().getHeight();
        }
        return generatedBackgrounds;
    }

    public float getLevelEndY() {
        return this.levelEndY;
    }

}
