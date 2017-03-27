package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
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

    public Player getPlayer() {
        return player;
    }

    @Override
    protected Camera initCamera() {
        return new OrthographicCamera();
    }

    @Override
    protected Viewport initViewport(Camera camera) {
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
        TaskManager.INSTANCE.removeTimers("difficultyIncrease");
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

        // Flame spawning
        addActor(new Actor() {

            private boolean waiting = true;

            @Override
            public void draw(Batch batch) {
            }

            @Override
            public void act() {
                if (waiting && GameScreen.this.player.getY() > 500) {
                    new FxFlameActor(GameScreen.this.player, 0, -100).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -80, -150).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -50, -200).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -30, -250).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -350).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -450).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -550).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -650).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -750).addToWorld(GameScreen.this);
                    TaskManager.INSTANCE.scheduleTimer(
                        "difficultyIncrease", 15, 15, GameScreen.this::difficultyIncrease);
                    waiting = false;
                    System.out.println("Flames started");
                }
            }

        });
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
        // Example implementation of difficulty increase: increasing gravity and speed of flames
        // NOTE: to be fair, maximum gravity should always allow player to jump vertically on a next platform
        this.world.setGravity(this.world.getGravity().cpy().scl(2.9f));
        if (this.world.getGravity().y < -94) {
            this.world.setGravity(new Vector2(0, -94));
        }
        for (Actor actor : getActors()) {
            if (actor instanceof FxFlameActor) {
                FxFlameActor flame = (FxFlameActor) actor;
                flame.increaseFlameVelocity(5);
            }
        }
        System.out.println("Difficulty increased, gravity is " + this.world.getGravity().y);
    }

}
