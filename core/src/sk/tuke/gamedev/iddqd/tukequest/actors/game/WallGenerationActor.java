package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;

/**
 * Created by macbook on 26/03/2017.
 */
public class WallGenerationActor implements Actor {
    public static int highestWallStartingY = 0;
    private static final int WALL_HEIGHT = 452;


    private Camera camera;
    private World world;

    public WallGenerationActor(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void act() {
        if (highestWallStartingY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {
            generateNextWalls();
            highestWallStartingY = highestWallStartingY + WALL_HEIGHT;
        }
        // TODO: implement some cleanUp logic here
    }

    public void generateNextWalls() {
        new BinaryVerticalWall(BinaryVerticalWall.Side.LEFT, highestWallStartingY, camera).addToWorld(this.world);
        new BinaryVerticalWall(BinaryVerticalWall.Side.RIGHT, highestWallStartingY, camera).addToWorld(this.world);
        System.out.println("Generated next walls!");
//        PlatformGenerator.generateNext(platformCount).forEach(platform -> platform.addToWorld(world));
//        System.out.println("PlatformGenerationActor generated "
//            + platformCount + " new platform" + (platformCount == 1 ? "" : "s") + "!");
    }
}
