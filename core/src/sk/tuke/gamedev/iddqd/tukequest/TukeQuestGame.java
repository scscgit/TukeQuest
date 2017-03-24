package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.ExampleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

public class TukeQuestGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize the World with a gravitation
        world = new World(new Vector2(0, -10), true);

        // Physics debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // Setup an orthographic camera to support a virtual screen size, which always maintains the same aspect ratio
        camera = new OrthographicCamera();
        viewport = new FitViewport(500, 500, camera);

        // Create example Actors in the World
        for (int i = 0; i < 3; i++) {
            new ExampleActor(
                new Animation("badlogic.jpg", 0.5f), BodyDef.BodyType.DynamicBody, i * 150, 300, camera
            ).addToWorld(world);
        }
        // Invisible ground
        new RectangleActor(Animation.INVISIBLE, BodyDef.BodyType.StaticBody, 0, 200, 500, 1)
            .addToWorld(world);
    }

    @Override
    public void render() {
        actOnActors();
        renderGraphics();
        calculatePhysics();
    }

    private void actOnActors() {
        Array<Body> worldBodies = new Array<Body>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.act();
        }
    }

    private void renderGraphics() {
        // Reset the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the images of all Actors
        batch.begin();
        Array<Body> worldBodies = new Array<Body>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.draw(batch);
        }
        batch.end();

        // Debug
        debugRenderer.render(world, camera.combined);
    }

    private void calculatePhysics() {
        // Progress physics, libGDX recommends timestep either 1/45f (which is 1/45th of a second) or 1/300f
        world.step(1 / 60f, 6, 2);

        // Create an array to be filled with the bodies (it's better not to create a new one every time though)
        Array<Body> worldBodies = new Array<Body>(0);
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

    // Screen resized event
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
