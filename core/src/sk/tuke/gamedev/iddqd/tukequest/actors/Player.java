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

    public static final Animation ANIMATION = new Animation("custom_player.png", 3, 3, 0, 8, 0.2f);

    private static final float DENSITY = .5f;
    private static final float FRICTION = .4f;
    private static final float INPUT_FORCE_MULTIPLIER = 200_000f;

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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump()) {
            getBody().applyLinearImpulse(new Vector2(0, INPUT_FORCE_MULTIPLIER), getCenter(), true);
        }

        // Toggle between camera debug mode and normal navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            this.cameraDebugMovementEnabled = !this.cameraDebugMovementEnabled;
        }

        if (this.cameraDebugMovementEnabled) {
            debugCameraMovement();
        } else {
            levelCameraOnPlayerPosition();
        }
    }

    private boolean canJump() {
        // TODO: fix
        return Math.abs(getBody().getLinearVelocity().y) < 0.1f;
    }

    /**
     * Make sure camera does not go below ground.
     */
    private void levelCameraOnPlayerPosition() {
        int calculatedY = (int) this.getY();
        if (calculatedY - (TukeQuestGame.SCREEN_HEIGHT / 2) <= 0) {
            calculatedY = TukeQuestGame.SCREEN_HEIGHT / 2;
        }
        this.camera.position.y = calculatedY;
        this.camera.position.x = TukeQuestGame.SCREEN_WIDTH / 2;
        this.camera.update();
    }

    private void debugCameraMovement() {
        // Apply force using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.camera.position.y = this.camera.position.y + 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.camera.position.y = this.camera.position.y - 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.camera.position.x = this.camera.position.x + 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.camera.position.x = this.camera.position.x - 10;
        }
        this.camera.update();
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
