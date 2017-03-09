package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import sk.tuke.gamedev.iddqd.tukequest.actors.Actor;
import sk.tuke.gamedev.iddqd.tukequest.actors.BodyActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.ExampleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

public class TukeQuestGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize the World with a gravitation
        world = new World(new Vector2(0, -10), true);

        // Load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
        TiledMap map = new TmxMapLoader().load("gameart2d.com-platformer/platformer-test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        debugRenderer = new Box2DDebugRenderer();

        // Create an orthographic camera, which shows us width and height of 20 units of the World
        camera = new OrthographicCamera();
        updateCamera();

        // Create a corresponding Actor for every map object
        for (MapObject object : map.getLayers().get("Object Layer 1").getObjects()) {
            createMapObject(object);
        }

        // Create an example Actor in the World
        new ExampleActor(
            new Animation("badlogic.jpg", 0.5f), BodyDef.BodyType.DynamicBody, 0, 300, camera
        ).addToWorld(world);
    }

    private void createMapObject(MapObject object) {
        // At the moment, we only support rectangles
        if (object instanceof RectangleMapObject) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleObject.getRectangle();

            new RectangleActor(
                Animation.INVISIBLE,
                BodyDef.BodyType.StaticBody,
                rectangle.getX(),
                rectangle.getY(),
                rectangle.getWidth(),
                rectangle.getHeight()
            ).addToWorld(this.world);
        } else {
            System.out.println("Warning: your map contains unexpected " + object.getClass().getName());
        }
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

        // Draw a background map
        renderer.setView(camera);
        renderer.render();

        // Debug
        debugRenderer.render(world, camera.combined.cpy().scl(1 / 32f));

        // Draw the images of all Actors
        batch.begin();
        Array<Body> worldBodies = new Array<Body>(0);
        world.getBodies(worldBodies);
        for (Body body : worldBodies) {
            Actor actor = (Actor) body.getUserData();
            actor.draw(batch);
        }
        batch.end();
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

    private void updateCamera() {
        camera.setToOrtho(false, 20, 20);
        camera.update();
    }

    // Screen resized event
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        updateCamera();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }
}
