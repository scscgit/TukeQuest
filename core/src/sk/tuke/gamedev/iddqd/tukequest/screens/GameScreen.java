package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.ExampleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 24.03.2017.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(TukeQuestGame game) {
        super(game);

        // Create example Actors in the World
        for (int i = 0; i < 3; i++) {
            new ExampleActor(
                new Animation("badlogic.jpg", 0.5f), BodyDef.BodyType.DynamicBody, i * 150, 300, camera
            ).addToWorld(this.world);
        }
        // Invisible ground
        new RectangleActor(Animation.INVISIBLE, BodyDef.BodyType.StaticBody, 0, 200, 500, 1)
            .addToWorld(this.world);
    }

    @Override
    protected Camera initCamera() {
        return new OrthographicCamera();
    }

    @Override
    protected FitViewport initViewport() {
        // Setup the camera to support a virtual screen size, which always maintains the same aspect ratio
        return new FitViewport(500, 500, camera);
    }

    @Override
    protected World initWorld() {
        // Initialize the World with a gravitation
        return new World(new Vector2(0, -10), true);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * Screen resized event.
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

}
