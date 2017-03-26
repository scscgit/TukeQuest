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
import sk.tuke.gamedev.iddqd.tukequest.actors.*;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.RenderFirst;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Enriches Screen by a physical interaction support and a default lifecycle implementation.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public abstract class AbstractScreen implements Screen {

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

    protected AbstractScreen(TukeQuestGame game) {
        this.game = game;
    }

    protected abstract Camera initCamera();

    protected abstract Viewport initViewport();

    protected abstract World initWorld();

    public World getWorld() {
        return this.world;
    }

    protected TukeQuestGame getGame() {
        return this.game;
    }

    public MyContactListener getWorldContactListener() {
        return this.worldContactListener;
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
        this.worldContactListener = new MyContactListener();
        this.world.setContactListener(this.worldContactListener);

        System.out.println("Screen " + this + " shown");
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public final void render(float delta) {
        addQueuedActors();
        actOnActors();
        renderGraphics();
        calculatePhysics();
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
    public Actor addActor(Actor actor) {
        this.addingActor = true;
        if (actor instanceof BodyActor) {
            ((BodyActor) actor).addToWorld(this);
        } else {
            // Adds them to an intermediate queue
            this.newActors.add(actor);
        }
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
        for (Actor actor : this.actors) {
            actor.act();
        }
        world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.act();
        }
    }

    protected void renderGraphics() {
        // Reset the screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the images of all Actors
        this.batch.begin();
        this.batch.setProjectionMatrix(this.camera.combined);
        List<Actor> lastRenderedActors = new LinkedList<>();
        for (Actor actor : this.actors) {
            if (actor instanceof RenderLast) {
                lastRenderedActors.add(actor);
                continue;
            }
            actor.draw(this.batch);
        }
        world.getBodies(this.temporaryWorldBodies);
        for (Body body : this.temporaryWorldBodies) {
            Actor actor = (Actor) body.getUserData();
            if (actor instanceof RenderLast) {
                lastRenderedActors.add(actor);
                continue;
            }
            actor.draw(this.batch);
        }
        for (Actor lastRenderedActor : lastRenderedActors) {
            lastRenderedActor.draw(this.batch);
        }
        this.batch.end();

        // Debug
        if (TukeQuestGame.debug) {
            debugRenderer.render(world, this.camera.combined.cpy().scl(AbstractBodyActor.SCALE_FROM_PHYSICS));
        }
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
        this.actors.clear();
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
