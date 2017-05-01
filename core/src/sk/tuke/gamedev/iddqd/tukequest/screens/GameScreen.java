package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlameMaster;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.KeyboardGround;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.assets.PlatformTexture;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.PlatformSize;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Binas;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Genci;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.visual.HUD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    public static final float DEFAULT_MUSIC_VOLUME = 0.2f;
    public static final float SILENT_MUSIC_VOLUME = 0.04f;

    // Vertical jump goes up 3 platforms
    public static final float GRAVITY = 70;
    // Vertical jump goes up 1 platform
    public static final float GRAVITY_LIMIT = 170;

    /**
     * Number of platforms displayed at once.
     */
    @Deprecated
    private static final int PLATFORMS_COUNT = 10;

    private Player player;
    private FxFlameMaster firstFlame;

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
        ScoreManager.INSTANCE = new ScoreManager();
        TaskManager.INSTANCE.removeTimers("difficultyIncrease");
        setHud(new HUD());
        PlatformGenerator.reset();
    }

    private void initActors() {
        // Place the ground as a basis below which no actors can be created
        float groundHeight = new KeyboardGround(0, 0).addToWorld(this).getAnimation().getHeight();


//        addActor(new VerticalActorGenerator(
//            this.camera, this, groundHeight, VerticalActorGenerator.BACKGROUND_FACTORY));
//
//        // Generate platforms starting from height of the ground
//        PlatformGenerator.highestPlatformY = groundHeight;
//        addActor(new VerticalActorGenerator(
//            this.camera, this, groundHeight, VerticalActorGenerator.PLATFORM_FACTORY));
//
//        // Generate walls at the sides as the camera moves
//        addActor(new VerticalActorGenerator(
//            this.camera, this, groundHeight, VerticalActorGenerator.LEFT_WALL_FACTORY));
//        addActor(new VerticalActorGenerator(
//            this.camera, this, groundHeight, VerticalActorGenerator.RIGHT_WALL_FACTORY));

        // Create Player standing at the KeyboardGround in the middle of the screen
        this.player = new Player(
            TukeQuestGame.SCREEN_WIDTH / 2 - Player.ANIMATION_STAND.getWidth() / 2,
            groundHeight,
            camera
        ).addToWorld(this);

        // Flame spawning and Exit handler
        addActor(new Actor() {

            private boolean waiting = true;

            @Override
            public void draw(Batch batch) {
            }

            @Override
            public void act() {
                if (waiting && (ScoreManager.INSTANCE.getCurrentScore() > 0 || GameScreen.this.player.getY() > 800)) {
                    FxFlameMaster firstFlame = new FxFlameMaster(
                        GameScreen.this.player, -30, -FxFlameMaster.MAX_DISTANCE);
                    GameScreen.this.firstFlame = firstFlame;
                    ScoreManager.INSTANCE.registerFxFlame(firstFlame);
                    firstFlame.registerHud(getHud());
                    firstFlame.addToWorld(GameScreen.this);
                    for (int i = 1; i < 40; i++) {
                        FxFlame otherFlame = new FxFlame(
                            GameScreen.this.player,
                            (35 * i) % 100 - 100,
                            -FxFlameMaster.MAX_DISTANCE - i * 50);
                        otherFlame.addToWorld(GameScreen.this);
                        firstFlame.addOtherFlame(otherFlame);
                    }
                    TaskManager.INSTANCE.scheduleTimer(
                        "difficultyIncrease", 15, 15, GameScreen.this::difficultyIncrease);
                    waiting = false;
                    Log.i(GameScreen.this, "Flames started");
                }
                if (InputHelper.isJustExit()) {
                    getGame().setScreen(new MenuScreen(getGame()));
                    throw new ScreenFinishedException();
                }
            }

        });

        List<Level> levels = createLevels();

        GameLevelManager gameLevelManager = new GameLevelManager(
            this,
            camera,
            groundHeight
                - Platform.ANIMATIONS.get(PlatformSize.LEVEL).get(PlatformTexture.MATHS).getHeight()
                - PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS,
            levels);
        addActor(gameLevelManager);
//        gameLevelGenerator.generateLevel();

//        addActor(gameLevelGenerator);
    }

    private List<Level> createLevels() {
        List<Level> levels = new ArrayList<>();
        levels.add(new Level("Binasov level", PlatformTexture.MATHS, Binas.class, null, null));
        levels.add(new Level("Genciho level", PlatformTexture.ROCK, Genci.class, null, null));
        levels.add(new Level("Porubanov level", PlatformTexture.CHIMNEY, Poruban.class, null, null));
        return levels;
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
        Vector2 newGravity = getGravity().scl(gravityMultiplier);
        if (newGravity.y < -GRAVITY_LIMIT) {
            newGravity.y = -GRAVITY_LIMIT;
        }
        setGravity(newGravity);
        this.firstFlame.increaseMinFlameVelocity(flameIncrease);
        Log.i(this, "Difficulty increased, gravity is " + this.world.getGravity().y);
    }

    public Vector2 getGravity() {
        return this.world.getGravity().cpy();
    }

    public void setGravity(Vector2 gravity) {
        this.world.setGravity(gravity);
        getHud().setGravity(gravity.y);
    }

    @Override
    public void dispose() {
        getActors().forEach(actor -> {
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        });
        super.dispose();
    }

}
