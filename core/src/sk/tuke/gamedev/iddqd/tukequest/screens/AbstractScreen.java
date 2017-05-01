package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.*;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.ActLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FullScreenImage;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.HUD;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Enriches Screen by a physical interaction support and a default lifecycle implementation.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public abstract class AbstractScreen implements Screen {

    public static final Animation PAUSED_ANIMATION = new Animation(
        "paused.jpg", Animation.ScaleType.SCALE_WIDTH, TukeQuestGame.SCREEN_HEIGHT);
    private static boolean renderGraphicsCycle;
    protected Camera camera;
    protected World world;
    protected Viewport viewport;
    private final Array<Body> temporaryWorldBodies = new Array<>();
    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private TukeQuestGame game;
    private MyContactListener worldContactListener;
    private List<Actor> actors = new LinkedList<>();
    private List<Actor> newActors = new LinkedList<>();
    private boolean addingActor;
    private Music music;
    private boolean paused;
    private HUD hud;

    protected AbstractScreen(TukeQuestGame game, Music music) {
        this.game = game;
        this.music = music;
        music.setLooping(true);
    }

    protected abstract Camera initCamera();

    protected abstract Viewport initViewport(Camera camera);

    protected abstract World initWorld();

    public World getWorld() {
        return this.world;
    }

    protected List<Actor> getActors() {
        return actors;
    }

    protected TukeQuestGame getGame() {
        return this.game;
    }

    public MyContactListener getWorldContactListener() {
        return this.worldContactListener;
    }

    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        // Physics debug renderer
        this.debugRenderer = new Box2DDebugRenderer();

        this.batch = new SpriteBatch();
        this.camera = initCamera();
        this.viewport = initViewport(this.camera);
        this.world = initWorld();
        if (this.world != null) {
            this.worldContactListener = new MyContactListener();
            this.world.setContactListener(this.worldContactListener);
        }
        this.music.play();
        Log.i(this, "Screen shown");
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        try {
            resetScreenColor();
            if (!isPaused()) {
                addQueuedActors();
                actOnActors();
            }
            this.batch.enableBlending();
            this.batch.begin();
            this.batch.setProjectionMatrix(this.camera.combined);
            if (isPaused()) {
                new FullScreenImage(PAUSED_ANIMATION, this.camera).draw(this.batch);
            } else {
                renderGraphics(this.batch);
            }
            this.batch.end();
            if (!isPaused()) {
                renderDebug();
                calculatePhysics();
            }
        } catch (ScreenFinishedException e) {
            Log.i(this, "Render interrupted as the screen has finished");
        }
    }

    protected void resetScreenColor() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * @return true if the addActor() is being currently processed to aggressively prevent breaking encapsulation.
     */
    public boolean isAddingActor() {
        return addingActor;
    }

    /**
     * Adding any {@link Actor} other than {@link BodyActor}, that should have its callbacks run during the game.
     */
    public final <T extends Actor> T addActor(T actor) {
        this.addingActor = true;
        if (actor instanceof BodyActor) {
            ((BodyActor) actor).addToWorld(this);
        }
        // Adds them to an intermediate queue
        this.newActors.add(actor);
        if (actor instanceof ActOnAdd) {
            ((ActOnAdd) actor).onAddedToScreen(this);
        }
        this.addingActor = false;
        return actor;
    }

    public AnimatedActor addActor(AnimatedActor actor) {
        addActor((Actor) actor);
        return actor;
    }

    public BodyActor addActor(BodyActor actor) {
        addActor((Actor) actor);
        return actor;
    }

    /**
     * Processes intermediate queue.
     */
    private void addQueuedActors() {
        if (this.newActors.isEmpty()) {
            return;
        }
        for (Actor actor : this.newActors) {
            // Actors meant to render first are moved back in the queue
            if (actor instanceof RenderFirst) {
                this.actors.add(0, actor);
            } else {
                this.actors.add(actor);
            }
        }
        this.newActors.clear();
    }

    protected void actOnActors() {
        List<Actor> lastActedActors = new LinkedList<>();
        for (Actor actor : this.actors) {
            if (actor instanceof ActLast) {
                lastActedActors.add(actor);
                continue;
            }
            actor.act();
        }
        if (this.world == null) {
            return;
        }
        lastActedActors.stream()
            .sorted(Comparator.comparingInt(actor -> ((ActLast) actor).getActLastOrder()))
            .forEach(Actor::act);
        if (this.hud != null) {
            this.hud.act();
        }
    }

    protected void renderGraphics(Batch batch) {
        AbstractScreen.renderGraphicsCycle = !AbstractScreen.renderGraphicsCycle;
        // Draw the images of all Actors
        List<Actor> lastRenderedActors = new LinkedList<>();
        for (Actor actor : this.actors) {
            if (actor instanceof RenderLast) {
                lastRenderedActors.add(actor);
                continue;
            }
            actor.draw(batch);
        }
        lastRenderedActors.stream()
            .sorted(Comparator.comparingInt(actor -> ((RenderLast) actor).getRenderLastOrder()))
            .forEach(actor -> actor.draw(batch));
        if (this.hud != null) {
            this.hud.draw(batch);
        }
    }

    /**
     * The boolean gets switched after every render cycle, this can be used by {@link Animation} to prevent
     * duplicate calculations on draw.
     */
    public static boolean getRenderGraphicsCycle() {
        return AbstractScreen.renderGraphicsCycle;
    }

    protected void renderDebug() {
        if (TukeQuestGame.debug && this.world != null) {
            this.debugRenderer.render(this.world, this.camera.combined.cpy().scl(AbstractBodyActor.SCALE_FROM_PHYSICS));
        }
    }

    protected void calculatePhysics() {
        if (this.world == null) {
            return;
        }
        // Progress physics, libGDX recommends timestep either 1/45f (which is 1/45th of a second) or 1/300f
        this.world.step(1 / 60f, 6, 2);

        this.world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            BodyActor actor = (BodyActor) body.getUserData();
            if (actor == null) {
                Log.w(this, "Non-physical actor has been added to the World");
                continue;
            }
            // Update the entities/sprites position and angle
            actor.setPositionFromBody();
            // We need to convert our angle from radians to degrees
            actor.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        }
    }

    /**
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        Log.i(this, "Screen resized");
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        Set<String> timers = TaskManager.INSTANCE.scheduledTimers();
        Log.i(this, "Screen paused" + (timers.size() == 0 ? "" : ", scheduled tasks:"));
        timers.forEach(timer -> System.out.println("> task " + timer));
        this.paused = true;
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        Set<String> timers = TaskManager.INSTANCE.scheduledTimers();
        Log.i(this, "Screen resumed" + (timers.size() == 0 ? "" : ", scheduled tasks:"));
        timers.forEach(timer -> System.out.println("> task " + timer));
        this.paused = false;
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        this.actors.clear();
        this.music.stop();
        Log.i(this, "Screen hidden");
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        this.batch.dispose();
        this.debugRenderer.dispose();
        this.world.dispose();
        Log.i(this, "Screen disposed");
    }

    public void setMusicVolume(float musicVolume) {
        if (musicVolume < 0 || musicVolume > 1) {
            throw new InvalidParameterException();
        }
        music.setVolume(musicVolume);
    }

    public void setHud(HUD hud) {
        ScoreManager.INSTANCE.setHud(hud);
        this.hud = hud;
    }

    public HUD getHud() {
        return this.hud;
    }

}
