package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.BinaryVerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.PlatformGenerationActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    /**
     * Number of platforms displayed at once.
     */
    private static final int PLATFORMS_COUNT = 10;

    private Player player;

    public GameScreen(TukeQuestGame game) {
        super(game);
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
        PlatformManager.INSTANCE = new PlatformManager(this.player);
    }

    private void initActors() {
        // Place ground and two vertical walls above it
        float groundHeight = new KeyboardGround(0, 0).addToWorld(this.world).getAnimation().getHeight();
        new BinaryVerticalWall(BinaryVerticalWall.Side.LEFT, groundHeight, camera).addToWorld(this.world);
        new BinaryVerticalWall(BinaryVerticalWall.Side.RIGHT, groundHeight, camera).addToWorld(this.world);

        // Generate platforms starting from height of the ground
        // TODO: put all generation code into PlatformGenerationActor
        PlatformGenerator.generateNext(PLATFORMS_COUNT, groundHeight).forEach(platform -> platform.addToWorld(world));
        addActor(new PlatformGenerationActor(camera, world));

        // Create Player standing at the KeyboardGround in the middle of the screen
        this.player = new Player(
            TukeQuestGame.SCREEN_WIDTH / 2 - Player.ANIMATION.getWidth() / 2,
            groundHeight,
            camera
        ).addToWorld(this.world);
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        super.hide();
        PlatformManager.INSTANCE = null;
    }

    public void difficultyIncrease() {
        // Example implementation of difficulty increase: increasing gravity
        System.out.println("Gravity increased");
        world.setGravity(world.getGravity().cpy().scl(1.7f));
    }

}
