package sk.tuke.gamedev.iddqd.tukequest.actors.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Surprise;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.GroundContactListener;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameOverScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 24.3.2017.
 */
public class Player extends RectangleActor implements RenderLast {

    //public static final Animation ANIMATION = new Animation("custom_player.png", 3, 3, 0, 8, 0.2f);

    public static final Animation ANIMATION_RIGHT_WALK = new Animation("pixeljoint.png", 8, 4, 0, 9, 0.2f);
    public static final Animation ANIMATION_RIGHT_JUMP = new Animation("pixeljoint.png", 8, 4, 10, 10, 0.2f);
    public static final Animation ANIMATION_RIGHT_STAND = new Animation("pixeljoint.png", 8, 4, 11, 12, 0.2f);

    public static final Animation ANIMATION_LEFT_WALK = new Animation("pixeljoint.png", 8, 4, 16, 25, 0.2f);
    public static final Animation ANIMATION_LEFT_JUMP = new Animation("pixeljoint.png", 8, 4, 26, 26, 0.2f);
    public static final Animation ANIMATION_LEFT_STAND = new Animation("pixeljoint.png", 8, 4, 27, 28, 0.2f);

    public static final Animation ANIMATION = ANIMATION_RIGHT_STAND;

    private static final float DENSITY = .1f;
    private static final float FRICTION = .0f;
    private static final float INPUT_FORCE_MULTIPLIER = 9_00f;
    private static final float JUMP_FORCE = 25_000f;
    private static final float SPRINT_VELOCITY_THRESHOLD = 50;
    private static final float SPRINT_BURST = 15_000f;

    private Camera camera;
    private boolean cameraDebugMovementEnabled;
    private GroundContactListener groundContactListener;
    private boolean sprint;
    private MovementController movementController;
    private boolean alive = true;
    private boolean jumping;

    public Player(float x, float y, Camera camera) {
        super(ANIMATION, BodyDef.BodyType.DynamicBody, x, y);
        this.camera = camera;
        this.movementController = new MovementController(INPUT_FORCE_MULTIPLIER, JUMP_FORCE, this, camera);
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
        if (jumping) {
            if (getAnimation().equals(ANIMATION_LEFT_STAND) || getAnimation().equals(ANIMATION_LEFT_WALK)) {
                setAnimation(ANIMATION_LEFT_JUMP);
            } else if (getAnimation().equals(ANIMATION_RIGHT_STAND) || getAnimation().equals(ANIMATION_RIGHT_WALK)) {
                setAnimation(ANIMATION_RIGHT_JUMP);
            }
        } else {
            if (getAnimation().equals(ANIMATION_LEFT_JUMP)) {
                setAnimation(ANIMATION_LEFT_STAND);
            } else if (getAnimation().equals(ANIMATION_RIGHT_JUMP)) {
                setAnimation(ANIMATION_RIGHT_STAND);
            }
        }
    }

    public void killedByFlame() {
        if (this.alive) {
            this.alive = false;
            System.out.println("Game over");
            getBody().getFixtureList().get(0).setSensor(true);
            this.movementController = null;
            TaskManager.INSTANCE.scheduleTimer("gameOverCountdown", 5,
                () -> TukeQuestGame.THIS.setScreen(new GameOverScreen(TukeQuestGame.THIS)));
        }
    }

    @Override
    public void act() {
        if (isOnGround()) {
            setJumping(false);
        }

        // Movement
        if (this.movementController != null) {
            this.movementController.act();
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

        if (canSprint()) {
            sprint();
        } else {
            stopSprintIfSlow();
        }
    }

    public boolean isOnGround() {
        return this.groundContactListener.isHitting();
    }

    private boolean canSprint() {
        return !sprint && isOnGround() && Math.abs(getBody().getLinearVelocity().x) > SPRINT_VELOCITY_THRESHOLD;
    }

    private void sprint() {
        float horizontalSpeed = getBody().getLinearVelocity().x;
        getBody().applyForceToCenter(new Vector2(
                horizontalSpeed + Math.signum(horizontalSpeed) * SPRINT_BURST,
                0),
            true);
        sprint = true;
        System.out.println(Player.class.getName() + " sprints");
    }

    private void stopSprintIfSlow() {
        if (sprint && Math.abs(getBody().getLinearVelocity().x) < SPRINT_VELOCITY_THRESHOLD) {
            sprint = false;
            System.out.println(Player.class.getName() + " no longer sprints");
        }
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
    protected void addContactHandlers(MyContactListener contactListener, Fixture fixture) {
        super.addContactHandlers(contactListener, fixture);
        this.groundContactListener = new GroundContactListener(fixture);
        contactListener.addHandler(this.groundContactListener);
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
    }

    public void collected(Surprise surprise) {
        getBody().applyForceToCenter(new Vector2(0f, 123456f), true);
    }

}
