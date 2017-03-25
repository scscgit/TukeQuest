package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 24.3.2017.
 */
public class Player extends RectangleActor {

    public static final Animation ANIMATION = new Animation("mario.png", 12, 1, 12, 0.2f);

    private static final float DENSITY = .5f;
    private static final float FRICTION = .4f;
    private static final float INPUT_FORCE_MULTIPLIER = 500000f;

    private Camera camera;

    public Player(float x, float y, Camera camera) {
        super(ANIMATION, BodyDef.BodyType.DynamicBody, x, y);
        this.camera = camera;
    }

    @Override
    public void act() {
        // Apply force using mouse
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Transform touch/mouse position coordinates to our camera's coordinate system
            this.camera.unproject(touchPos);

            //setX(touchPos.x + 64);
            float moveX = (touchPos.x - camera.viewportWidth / 2) / camera.viewportWidth * INPUT_FORCE_MULTIPLIER;
            getBody().applyForceToCenter(new Vector2(moveX, 0f), true);
        }

        // Apply force using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //setX(getX() - 5 * Gdx.graphics.getDeltaTime());
            getBody().applyForceToCenter(
                new Vector2(-50 * Gdx.graphics.getDeltaTime() * INPUT_FORCE_MULTIPLIER, 0f), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //setX(getX() + 5 * Gdx.graphics.getDeltaTime());
            getBody().applyForceToCenter(
                new Vector2(50 * Gdx.graphics.getDeltaTime() * INPUT_FORCE_MULTIPLIER, 0f), true);
        }

        // Jump
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Math.abs(getBody().getLinearVelocity().y) < 0.1f) {
            getBody().setLinearVelocity(new Vector2(0, 1000_000f));
        }
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
    }

}
