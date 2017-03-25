package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
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

/**
 * Created by Steve on 24.03.2017.
 */
public abstract class AbstractScreen implements Screen {

    private final SpriteBatch batch;
    private final Box2DDebugRenderer debugRenderer;
    private TukeQuestGame game;
    protected Camera camera;
    protected World world;
    protected Viewport viewport;

    protected AbstractScreen(TukeQuestGame game) {
        batch = new SpriteBatch();

        // Physics debug renderer
        debugRenderer = new Box2DDebugRenderer();

        this.game = game;
        this.camera = initCamera();
        this.viewport = initViewport();
        this.world = initWorld();
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

    protected void actOnActors() {
        Array<Body> worldBodies = new Array<>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.act();
        }
    }

    protected void renderGraphics() {
        // Reset the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the images of all Actors
        this.batch.begin();
        this.batch.setProjectionMatrix(camera.combined);
        Array<Body> worldBodies = new Array<>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.draw(this.batch);
        }
        this.batch.end();

        // Debug
        if (TukeQuestGame.debug) {
            debugRenderer.render(world, camera.combined);
        }
    }

    protected void calculatePhysics() {
        // Progress physics, libGDX recommends timestep either 1/45f (which is 1/45th of a second) or 1/300f
        world.step(1 / 60f, 6, 2);

        // Create an array to be filled with the bodies (it's better not to create a new one every time though)
        Array<Body> worldBodies = new Array<>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
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
        batch.dispose();
        debugRenderer.dispose();
        world.dispose();
        System.out.println("Screen " + this + " disposed");
    }

}
