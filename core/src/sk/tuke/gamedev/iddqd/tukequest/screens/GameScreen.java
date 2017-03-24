package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.BinaryVerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(TukeQuestGame game) {
        super(game);
        initActors();
        initScheduling();
    }

    private void initActors() {
        // Create example Actors in the World
        new Player(
            50,
            300,
            camera
        ).addToWorld(this.world);

        // Invisible grounds
        new RectangleActor(Animation.INVISIBLE, BodyDef.BodyType.StaticBody, 0, 200, 200, 1)
            .addToWorld(this.world);
        new RectangleActor(Animation.INVISIBLE, BodyDef.BodyType.StaticBody, 200, 150, 1, 50)
            .addToWorld(this.world);

        // Places ground and two vertical walls above it
        float groundHeight = new KeyboardGround(0, 0).addToWorld(this.world).getAnimation().getHeight();
        new BinaryVerticalWall(BinaryVerticalWall.Side.LEFT, groundHeight).addToWorld(this.world);
        new BinaryVerticalWall(BinaryVerticalWall.Side.RIGHT, groundHeight).addToWorld(this.world);
    }

    private void initScheduling() {
        String namespace = "timedDifficulty";

        // Every 2 seconds the difficulty gets increased
        TaskManager.INSTANCE.scheduleTimer(namespace, 2, 2, this::difficultyIncrease);

        // After 7 seconds it stops being increased
        TaskManager.INSTANCE.scheduleTimer(namespace, 9, () -> {
            TaskManager.INSTANCE.removeTimers(namespace);
            System.out.println("Difficulty increasing stopped");
        });
    }

    @Override
    protected Camera initCamera() {
        return new OrthographicCamera();
    }

    @Override
    protected FitViewport initViewport() {
        // Setup the camera to support a virtual screen size, which always maintains the same aspect ratio
        return new FitViewport(500, 500, camera);
    }

    @Override
    protected World initWorld() {
        // Initialize the World with a gravitation
        return new World(new Vector2(0, -10), true);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * Screen resized event.
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

    public void difficultyIncrease() {
        // Example implementation of difficulty increase: increasing gravity
        System.out.println("Difficulty increased");
        world.setGravity(world.getGravity().cpy().scl(5.5f));
    }

}
