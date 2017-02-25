package sk.tuke.gamedev.iddqd.tukequest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class TukeQuestGame extends ApplicationAdapter {

    private static final float IMG_SCALE = 0.5f;

    private SpriteBatch batch;
    private Texture img;
    private float imgX = 50;
    private float imgY = 50;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        // Load the map, set the unit scale to 1/32 (1 unit == 32 pixels)
        TiledMap map = new TmxMapLoader().load("gameart2d.com-platformer/platformer-test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);

        // Create an orthographic camera, which shows us width of 20 units of the world and a corresponding height
        camera = new OrthographicCamera();
        updateCamera();
    }

    @Override
    public void render() {
        // Reset the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Move the image using mouse
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Transform touch/mouse position coordinates to our camera's coordinate system
            camera.unproject(touchPos);

            imgX = touchPos.x - 64;
        }

        // Move the image using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            imgX -= 5 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            imgX += 5 * Gdx.graphics.getDeltaTime();
        }

        // Draw a background map
        renderer.setView(camera);
        renderer.render();

        // Draw the image
        batch.begin();
        batch.draw(img, imgX, imgY, img.getWidth() * IMG_SCALE, img.getHeight() * IMG_SCALE);
        batch.end();
    }

    private void updateCamera() {
        float heightRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera.setToOrtho(false, 20, 20 * heightRatio);
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
        img.dispose();
        renderer.dispose();
    }
}
