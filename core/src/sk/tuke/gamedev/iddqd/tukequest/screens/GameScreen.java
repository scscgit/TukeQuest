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
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.Platform;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.PlatformSize;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.Player;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers.Poruban;
import sk.tuke.gamedev.iddqd.tukequest.generator.CollectableGenerator;
import sk.tuke.gamedev.iddqd.tukequest.generator.PlatformGenerator;
import sk.tuke.gamedev.iddqd.tukequest.levels.Level;
import sk.tuke.gamedev.iddqd.tukequest.levels.Levels;
import sk.tuke.gamedev.iddqd.tukequest.managers.GameLevelManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.util.InputHelper;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    public static final float DEFAULT_MUSIC_VOLUME = 0.2f;
    public static final float SILENT_MUSIC_VOLUME = 0.04f;

    // Vertical jump goes up 3 platforms
    public static final float GRAVITY_START = 70;
    // Vertical jump goes up 1 platform
    public static final float GRAVITY_LIMIT = 140;

    private static final float DIFFICULTY_INCREASE_GRAVITY_MULTIPLIER = 1.1f;
    private static final float DIFFICULTY_INCREASE_FLAME_INCREASE = 0.4f;

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
        return new World(new Vector2(0, -GRAVITY_START), true);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
        Poruban.isPorubanLevel = false;
        ScoreManager.INSTANCE = new ScoreManager();
        TaskManager.INSTANCE.removeTimers("difficultyIncrease");
        CollectableGenerator.reset();
        // Initialize actors
        initActors();
        PlatformManager.INSTANCE = new PlatformManager(this.player);
        // Create the HUD after player's camera position is initialized, so that his movement can be detected
        setHud(this.player.createHud());
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
            camera,
            this

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
                        "difficultyIncrease", 4, 4, GameScreen.this::difficultyIncrease);
                    waiting = false;
                    Log.i(GameScreen.this, "Flames started");
                }
                if (InputHelper.isJustExit()) {
                    getGame().setScreen(new MenuScreen(getGame()));
                    throw new ScreenFinishedException();
                }
                if (InputHelper.isHelp()) {
                    getGame().setScreen(new TutorialScreen(getGame()));
                }
            }

        });

        List<Level> levels = createLevels();

        GameLevelManager gameLevelManager = new GameLevelManager(
            this,
            camera,
            groundHeight
                - Platform.ANIMATIONS.get(PlatformSize.LEVEL).get(Platform.PlatformTexture.MATHS).getHeight()
                - PlatformGenerator.Y_DISTANCE_BETWEEN_PLATFORMS,
            levels);
        addActor(gameLevelManager);
//        gameLevelGenerator.generateLevel();

//        addActor(gameLevelGenerator);
    }

    private List<Level> createLevels() {
        List<Level> levels = new ArrayList<>();
        // TODO: implement more levels with reasonable backgrounds
        for (Levels levelsValue : Levels.values()) {
            levels.add(levelsValue.level);
        }
        return levels;
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        super.hide();
        TaskManager.INSTANCE.removeTimers("gameOverCountdown");
        PlatformManager.INSTANCE = null;
    }

    public void difficultyIncrease() {
        // Example implementation of difficulty increase: increasing gravity and speed of flames
        // NOTE: to be fair, maximum gravity should always allow player to jump vertically on a next platform
        Vector2 newGravity = getGravity().scl(DIFFICULTY_INCREASE_GRAVITY_MULTIPLIER);
        if (newGravity.y < -GRAVITY_LIMIT) {
            newGravity.y = -GRAVITY_LIMIT;
        }
        setGravity(newGravity);
        this.firstFlame.increaseMinFlameVelocity(DIFFICULTY_INCREASE_FLAME_INCREASE);
        Log.i(
            this,
            "Difficulty increased, gravity is " + this.world.getGravity().y
                + ", flame speed is " + this.firstFlame.getMinSpeed());
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

    public FxFlameMaster getFirstFlame() {
        return firstFlame;
    }

}
