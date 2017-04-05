package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlameActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalActorGenerator;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Genci;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    // Vertical jump goes up 4 platforms
    private static final float GRAVITY = 75;
    // Vertical jump goes up 1 platform
    private static final float GRAVITY_LIMIT = 170;

    /**
     * Number of platforms displayed at once.
     */
    @Deprecated
    private static final int PLATFORMS_COUNT = 10;

    private Player player;

    public GameScreen(TukeQuestGame game) {
        super(game, TukeQuestGame.manager.get("audio/music/backgroundmusic.mp3", Music.class));
        setMusicVolume(0.2f);
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
        return new FitViewport(TukeQuestGame.SCREEN_WIDTH, TukeQuestGame.SCREEN_HEIGHT, camera);
    }

    @Override
    protected World initWorld() {
        // Initialize the World with a gravitation
        return new World(new Vector2(0, -GRAVITY), true);
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
            TukeQuestGame.SCREEN_WIDTH / 2 - Player.ANIMATION_STAND.getWidth() / 2,
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
                if (waiting && GameScreen.this.player.getY() > 800) {
                    new FxFlameActor(GameScreen.this.player, 0, -100).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -80, -150).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -50, -200).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -30, -250).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -10, -350).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -50, -450).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -40, -550).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -20, -650).addToWorld(GameScreen.this);
                    new FxFlameActor(GameScreen.this.player, -90, -750).addToWorld(GameScreen.this);
                    TaskManager.INSTANCE.scheduleTimer(
                        "difficultyIncrease", 15, 15, GameScreen.this::difficultyIncrease);
                    waiting = false;
                    Log.i(GameScreen.this, "Flames started");
                }
            }

        });

        new Genci(this.player, TukeQuestGame.SCREEN_WIDTH * 3.3f / 5f, groundHeight)
            .addToWorld(this);
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
        float gravityMultiplier = 1.3f;
        float flameIncrease = 0.5f;

        // Example implementation of difficulty increase: increasing gravity and speed of flames
        // NOTE: to be fair, maximum gravity should always allow player to jump vertically on a next platform
        this.world.setGravity(this.world.getGravity().cpy().scl(gravityMultiplier));
        if (this.world.getGravity().y < -GRAVITY_LIMIT) {
            this.world.setGravity(new Vector2(0, -GRAVITY_LIMIT));
        }
        Array<Body> temporaryBodies = new Array<>();
        getWorld().getBodies(temporaryBodies);
        // TODO: probably use getActors() instead, which means addActor will add them to actors field of Screen
        for (Body body : temporaryBodies) {
            if (body.getUserData() instanceof FxFlameActor) {
                FxFlameActor flame = (FxFlameActor) body.getUserData();
                flame.increaseFlameVelocity(flameIncrease);
            }
        }
        Log.i(this, "Difficulty increased, gravity is " + this.world.getGravity().y);
    }

}
