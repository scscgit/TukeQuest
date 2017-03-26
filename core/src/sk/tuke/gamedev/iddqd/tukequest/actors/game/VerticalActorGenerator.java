package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.AnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;

/**
 * Created by Steve on 26.03.2017.
 */
public class VerticalActorGenerator implements Actor {

    public interface ActorFactory {
        AnimatedActor createActor(AbstractScreen screen, float y);
    }

    public static final ActorFactory BACKGROUND_FACTORY = (screen, y) ->
        screen.addActor(new Background(y));

    public static final ActorFactory LEFT_WALL_FACTORY = (screen, y) ->
        new BinaryVerticalWall(BinaryVerticalWall.Side.LEFT, y).addToWorld(screen);
    public static final ActorFactory RIGHT_WALL_FACTORY = (screen, y) ->
        new BinaryVerticalWall(BinaryVerticalWall.Side.RIGHT, y).addToWorld(screen);

    public static final ActorFactory PLATFORM_FACTORY = (screen, y) ->
        PlatformGenerator.generateNext().addToWorld(screen);

    private Camera camera;
    private AbstractScreen screen;
    private float nextStartingY;
    private ActorFactory factory;

    public VerticalActorGenerator(Camera camera, AbstractScreen screen, float nextStartingY, ActorFactory factory) {
        this.camera = camera;
        this.screen = screen;
        this.nextStartingY = nextStartingY;
        this.factory = factory;
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void act() {
        if (this.nextStartingY < this.camera.position.y + TukeQuestGame.SCREEN_HEIGHT) {
            AnimatedActor actor = this.factory.createActor(this.screen, this.nextStartingY);
            System.out.println("Generated next " + actor.getClass().getSimpleName() + " at " + this.nextStartingY);
            this.nextStartingY += actor.getAnimation().getHeight();
        }
        // TODO: implement some cleanUp logic here
    }

}
