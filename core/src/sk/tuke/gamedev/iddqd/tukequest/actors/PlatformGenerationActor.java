package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;

/**
 * Created by macbook on 25/03/2017.
 */
public class PlatformGenerationActor implements Actor {

    private Camera camera;
    private World world;

    public PlatformGenerationActor(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void act() {
        if (PlatformGenerator.highestPlatformY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {
            generate(1);
        }
        // TODO: implement some cleanUp logic here
    }

    public void generate(int platformCount) {
        PlatformGenerator.generateNext(platformCount).forEach(platform -> platform.addToWorld(world));
        System.out.println("PlatformGenerationActor generated "
            + platformCount + " new platform" + (platformCount == 1 ? "" : "s") + "!");
    }

}
