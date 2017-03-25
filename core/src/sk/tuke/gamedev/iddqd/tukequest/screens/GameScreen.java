package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.BinaryVerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.PlatformGenerationActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.Player;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(TukeQuestGame game) {
        super(game);
    }

    private void initActors() {

        // Places ground and two vertical walls above it
        float groundHeight = new KeyboardGround(0, 0).addToWorld(this.world).getAnimation().getHeight();
        new BinaryVerticalWall(BinaryVerticalWall.Side.LEFT, groundHeight, camera).addToWorld(this.world);
        new BinaryVerticalWall(BinaryVerticalWall.Side.RIGHT, groundHeight, camera).addToWorld(this.world);


        // generate 10 platforms starting from groundHeight
        int PLATFORMS_COUNT = 10;
        int PLATFORMS_STARTING_Y = (int) groundHeight;

        PlatformGenerator.generateNext(PLATFORMS_COUNT, PLATFORMS_STARTING_Y).forEach(platform -> platform.addToWorld(world));

        // Create Player standing at the KeyboardGround in the middle of the screen
        Player player = new Player(
            TukeQuestGame.SCREEN_WIDTH / 2,
            groundHeight,
            camera
        ).addToWorld(this.world);

        PlatformManager.INSTANCE = new PlatformManager(player);

        addActor(new PlatformGenerationActor(camera, world));
    }



    private void initScheduling() {
    }

    @Override
    protected Camera initCamera() {
        return new OrthographicCamera();
    }

    @Override
    protected Viewport initViewport() {
        // Setup the camera to support a virtual screen size, which always maintains the same aspect ratio
        return new FitViewport(500, 500, camera);
    }

    @Override
    protected World initWorld() {
        // Initialize the World with a gravitation
        return new World(new Vector2(0, -20), true);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
        initActors();
        initScheduling();
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
        System.out.println("Gravity increased");
        world.setGravity(world.getGravity().cpy().scl(1.7f));
    }

}
