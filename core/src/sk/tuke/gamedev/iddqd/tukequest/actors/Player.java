package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 24.3.2017.
 */
public class Player extends RectangleActor {

    public static final Animation ANIMATION = new Animation("mario.png", 12, 1, 12, 0.2f);

    private static final float DENSITY = .5f;
    private static final float FRICTION = .4f;
    private static final float INPUT_FORCE_MULTIPLIER = 1_000_000f;

    private Camera camera;
    private boolean cameraDebugMovementEnabled = false;

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
            getBody().applyLinearImpulse(new Vector2(0, INPUT_FORCE_MULTIPLIER), getCenter(), true);
        }

        // togle between camera debug mode and normal navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            cameraDebugMovementEnabled = !cameraDebugMovementEnabled;
        }

        if (cameraDebugMovementEnabled) {
            debugCameraMovement();

        } else {
            levelCameraOnPlayerPosition();
        }
    }

    private void levelCameraOnPlayerPosition() {

        // make sure camera does not go below ground

//        camera.position.x = this.getX();
        int calculatedY = (int) this.getY();
        if (calculatedY - (TukeQuestGame.SCREEN_HEIGHT / 2) <= 0) {
            calculatedY = TukeQuestGame.SCREEN_HEIGHT / 2;
        }
        camera.position.y = calculatedY;
        camera.position.x = TukeQuestGame.SCREEN_WIDTH / 2;
        camera.update();
    }

    private void debugCameraMovement() {
        // Apply force using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y = camera.position.y + 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y = camera.position.y - 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x = camera.position.x + 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x = camera.position.x - 10;
        }

        camera.update();
    }

    @Override
    protected void configureBodyDef(BodyDef bodyDef) {
        // Player cannot rotate
        bodyDef.fixedRotation = true;
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
    }

}
