package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlameActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalActorGenerator;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;

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
        return new World(new Vector2(0, -50), true);
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
        // Place the ground as a basis below which no actors can be created
        float groundHeight = new KeyboardGround(0, 0).addToWorld(this).getAnimation().getHeight();



        addActor(new VerticalActorGenerator(
            this.camera, this, groundHeight, VerticalActorGenerator.BACKGROUND_FACTORY));

        // Generate platforms starting from height of the ground
        PlatformGenerator.highestPlatformY = groundHeight;
        addActor(new VerticalActorGenerator(
            this.camera, this, groundHeight, VerticalActorGenerator.PLATFORM_FACTORY));

        // Generate walls at the sides as the camera moves
        addActor(new VerticalActorGenerator(
            this.camera, this, groundHeight, VerticalActorGenerator.LEFT_WALL_FACTORY));
        addActor(new VerticalActorGenerator(
            this.camera, this, groundHeight, VerticalActorGenerator.RIGHT_WALL_FACTORY));

        // Create Player standing at the KeyboardGround in the middle of the screen
        this.player = new Player(
            TukeQuestGame.SCREEN_WIDTH / 2 - Player.ANIMATION.getWidth() / 2,
            groundHeight,
            camera
        ).addToWorld(this);

        FxFlameActor flameActor = new FxFlameActor(0, -100).addToWorld(this);

        // todo: move flames
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        super.hide();
        PlatformManager.INSTANCE = null;
    }

    @Deprecated
    public void difficultyIncrease() {
        // Example implementation of difficulty increase: increasing gravity
        System.out.println("Gravity increased");
        world.setGravity(world.getGravity().cpy().scl(1.7f));
    }

}
