package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Background;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalActorGenerator;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Collectable;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.generator.CollectableGenerator;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.generator.TeacherGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameLevelGenerator {

    private final Level level;
    private GameScreen screen;
    private float startingY;
    private float levelEndY;

    public GameLevelGenerator(GameScreen screen, Level level, float startingY) {
        this.screen = screen;
        this.startingY = startingY;
        this.level = level;
    }

    public List<Actor> generateLevel() {

        System.out.println("Generating LEVEL = " + level.LEVEL_NAME);

        List<Actor> generatedActors = new ArrayList<>();

        levelEndY = startingY + 50 * PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS;

        // TODO: This factory should not be added with each level
//        screen.addActor(new VerticalActorGenerator(
//            this.camera, screen, startingY, VerticalActorGenerator.BACKGROUND_FACTORY));
        generatedActors.addAll(generateBackgrounds());

        // Generate platforms starting from height of the ground
        PlatformGenerator.highestPlatformY = startingY;

        List<Platform> levelPlatforms = PlatformGenerator.generateNext(level.PLATFORM_TEXTURE);
        generatedActors.addAll(levelPlatforms);
        Platform firstPlatform = levelPlatforms.get(0);

        for (int i = 1; i < levelPlatforms.size(); i++) {
            Platform platform = levelPlatforms.get(i);
            Collectable collectable = CollectableGenerator.createForPlatform(platform);
            if (collectable != null) {
                generatedActors.add(collectable);
            }
        }

        // generate teacher on first platform (this should be the BIG one)
//        generatedActors.add(TeacherGenerator.generateRandomTeacherOnPlatform(firstPlatform, screen));
        generatedActors.add(TeacherGenerator.generateTeacherOnPlatform(level.TEACHER_CLASS, firstPlatform, screen));

        // generate walls at the sides
        generatedActors.addAll(generateWalls());


        return generatedActors;
    }


    // generate walls for whole level\
    private List<VerticalWall> generateWalls() {
        List<VerticalWall> generatedWalls = new ArrayList<>();
        float wallsMaxY = startingY;

        while (wallsMaxY < levelEndY) {
            VerticalWall leftWall = new VerticalWall(VerticalWall.Side.LEFT, wallsMaxY);
            VerticalWall rightWall = new VerticalWall(VerticalWall.Side.RIGHT, wallsMaxY);

            generatedWalls.add(leftWall);
            generatedWalls.add(rightWall);

            wallsMaxY = wallsMaxY + leftWall.getHeight();
            System.out.println("New wallsMaxY " + wallsMaxY);
        }

        return generatedWalls;
    }

    private List<Background> generateBackgrounds() {
        List<Background> generatedBackgrounds = new ArrayList<>();
        float backgroundMaxY = startingY;

        while (backgroundMaxY < levelEndY) {
            Background background = new Background(backgroundMaxY);

            generatedBackgrounds.add(background);

            backgroundMaxY = backgroundMaxY + background.getAnimation().getHeight();
        }
        return generatedBackgrounds;
    }

    public float getLevelEndY() {
        return levelEndY;
    }
}
