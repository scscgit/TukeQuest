package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.graphics.Camera;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
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

    private GameScreen screen;
    private Camera camera;
    private float startingY;
    private float levelEndY;

    public GameLevelGenerator(GameScreen screen, Camera camera, float startingY) {
        this.screen = screen;
        this.camera = camera;
        this.startingY = startingY;
    }

    public List<BodyActor> generateLevel() {
        List<BodyActor> generatedActors = new ArrayList<>();

        levelEndY = startingY + 50 * PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS;

        // TODO: This factory should not be added with each level
        screen.addActor(new VerticalActorGenerator(
            this.camera, screen, startingY, VerticalActorGenerator.BACKGROUND_FACTORY));

        // Generate platforms starting from height of the ground
        PlatformGenerator.highestPlatformY = startingY;

        List<Platform> levelPlatforms = PlatformGenerator.generateNext();
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
        generatedActors.add(TeacherGenerator.generateRandomTeacherOnPlatform(firstPlatform, screen));

        // generate walls at the sides
        generateWalls(generatedActors);

        return generatedActors;
    }


    // generate walls for whole level\
    private void generateWalls(List<BodyActor> generatedActors) {
        float wallsMaxY = startingY;

        while (wallsMaxY < levelEndY) {
            VerticalWall leftWall = new VerticalWall(VerticalWall.Side.LEFT, wallsMaxY);
            VerticalWall rightWall = new VerticalWall(VerticalWall.Side.RIGHT, wallsMaxY);

            generatedActors.add(leftWall);
            generatedActors.add(rightWall);

            wallsMaxY = wallsMaxY + leftWall.getHeight();
            System.out.println("New wallsMaxY " + wallsMaxY);
        }
    }

    public float getLevelEndY() {
        return levelEndY;
    }
}
