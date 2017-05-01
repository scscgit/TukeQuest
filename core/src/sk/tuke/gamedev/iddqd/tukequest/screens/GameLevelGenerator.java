package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalActorGenerator;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.generator.TeacherGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameLevelGenerator {

    private AbstractScreen screen;

    private Camera camera;

    private float startingY;

    private float levelEndY;


    private List<BodyActor> generatedActors = new ArrayList<>();

    public GameLevelGenerator(AbstractScreen screen, Camera camera, float startingY) {
        this.screen = screen;
        this.camera = camera;
        this.startingY = startingY;
    }


    public List<BodyActor> generateLevel() {

        levelEndY = startingY + 50 * PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS;

        screen.addActor(new VerticalActorGenerator(
            this.camera, screen, startingY, VerticalActorGenerator.BACKGROUND_FACTORY));

        // Generate platforms starting from height of the ground
        PlatformGenerator.highestPlatformY = startingY;
//        screen.addActor(new VerticalActorGenerator(
//            this.camera, screen, startingY, VerticalActorGenerator.PLATFORM_FACTORY));

        List<Platform> levelPlatforms = PlatformGenerator.generateNext(50);
        generatedActors.addAll(levelPlatforms);
        Platform firstPlatform = levelPlatforms.get(0);


        generatedActors.add(TeacherGenerator.generateRandomTeacherOnPlatform(firstPlatform));

//        TeacherGenerator.generateTeacherIfNeeded(startingY + Platform.);

        // generate walls at the sides
        generateWalls();

        // Generate walls at the sides as the camera moves
//        screen.addActor(new VerticalActorGenerator(
//            camera, screen, startingY, VerticalActorGenerator.LEFT_WALL_FACTORY));
//        screen.addActor(new VerticalActorGenerator(
//            camera, screen, startingY, VerticalActorGenerator.RIGHT_WALL_FACTORY));

        return generatedActors;
    }


    // generate walls for whole level\
    private void generateWalls() {
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
