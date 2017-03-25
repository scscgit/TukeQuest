package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Steve on 24.03.2017.
 */
public abstract class AbstractScreen implements Screen {

    public static Texture backgroundTexture = new Texture("background.jpg");

    protected Camera camera;
    protected World world;
    protected Viewport viewport;
    private final Array<Body> temporaryWorldBodies = new Array<>();
    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private TukeQuestGame game;
    private Set<Actor> actors = new HashSet<>();

    protected AbstractScreen(TukeQuestGame game) {
        this.game = game;
    }

    protected abstract Camera initCamera();

    protected abstract Viewport initViewport();

    protected abstract World initWorld();

    protected TukeQuestGame getGame() {
        return this.game;
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
        this.viewport = initViewport();
        this.world = initWorld();

        System.out.println("Screen " + this + " shown");
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public final void render(float delta) {
        actOnActors();
        renderGraphics();
        calculatePhysics();
    }

    /**
     * Adding any {@link Actor} other than {@link BodyActor}, that should have its callbacks run.
     */
    public void addActor(Actor actor) {
        if (actor instanceof BodyActor) {
            throw new IllegalArgumentException("BodyActors should be added to world!");
        }
        this.actors.add(actor);
    }

    protected void actOnActors() {
        world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.act();
        }
        for (Actor actor : this.actors) {
            actor.act();
        }
    }

    protected void renderGraphics() {
        // Reset the screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the images of all Actors
        this.batch.begin();
        renderBackground();
        this.batch.setProjectionMatrix(this.camera.combined);
        world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.draw(this.batch);
        }
        for (Actor actor : this.actors) {
            actor.draw(this.batch);
        }
        this.batch.end();

        // Debug
        if (TukeQuestGame.debug) {
            debugRenderer.render(world, this.camera.combined);
        }
    }

    protected void renderBackground() {
        float imageX = this.camera.position.x - this.camera.viewportWidth / 2;
        float imageY = this.camera.position.y - this.camera.viewportHeight / 2;
        batch.draw(backgroundTexture, imageX, imageY);
    }

    protected void calculatePhysics() {
        // Progress physics, libGDX recommends timestep either 1/45f (which is 1/45th of a second) or 1/300f
        world.step(1 / 60f, 6, 2);

        world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            BodyActor actor = (BodyActor) body.getUserData();
            if (actor == null) {
                System.out.println("Warning: non-physical actor has been added to the World");
                continue;
            }
            // Update the entities/sprites position and angle
            actor.setPosition(body.getPosition());
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
        System.out.println("Screen " + this + " resized");
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        System.out.println("Screen " + this + " paused");
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        System.out.println("Screen " + this + " resumed");
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        System.out.println("Screen " + this + " hidden");
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        this.batch.dispose();
        this.debugRenderer.dispose();
        this.world.dispose();
        System.out.println("Screen " + this + " disposed");
    }

}
