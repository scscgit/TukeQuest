package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 08.03.2017.
 */
public class ExampleActor extends AbstractBodyActor {

    private static final float DENSITY = .5f;
    private static final float FRICTION = .4f;
    private static final float RESTITUTION = .6f;
    private static final float INPUT_FORCE_MULTIPLIER = 500000f;

    private Camera camera;

    public ExampleActor(Animation animation, BodyDef.BodyType bodyType, float x, float y, Camera camera) {
        super(animation, bodyType, x, y);
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

        // Jump (fly)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            getBody().applyLinearImpulse(new Vector2(0, 10000f), getPosition(), true);
        }
    }

    protected Shape createShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(getAnimation().getWidth() / 2);
        // Shape radius will start in the center
        shape.setPosition(new Vector2(getAnimation().getWidth() / 2, getAnimation().getHeight() / 2));
        return shape;
    }

    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        // Make it bounce a little bit
        fixtureDef.restitution = RESTITUTION;
    }

}
