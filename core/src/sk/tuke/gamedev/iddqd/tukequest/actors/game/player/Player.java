package sk.tuke.gamedev.iddqd.tukequest.actors.game.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Collectable;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Surprise;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.CameraFollow;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Command;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.HorizontalMovement;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Jump;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.GroundContactHandler;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameOverScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 24.3.2017.
 */
public class Player extends RectangleActor implements RenderLast {

    //public static final Animation ANIMATION = new Animation("custom_player.png", 3, 3, 0, 8, 0.2f);

    private static final String ANIMATION_FILE_NAME = "pixeljoint.png";
    public static final Animation ANIMATION_RIGHT_WALK = new Animation(ANIMATION_FILE_NAME, 8, 4, 0, 9, 0.2f);
    public static final Animation ANIMATION_RIGHT_JUMP = new Animation(ANIMATION_FILE_NAME, 8, 4, 10, 10, 0.2f);
    public static final Animation ANIMATION_RIGHT_STAND = new Animation(ANIMATION_FILE_NAME, 8, 4, 11, 12, 0.2f);

    public static final Animation ANIMATION_LEFT_WALK = new Animation(ANIMATION_FILE_NAME, 8, 4, 16, 25, 0.2f);
    public static final Animation ANIMATION_LEFT_JUMP = new Animation(ANIMATION_FILE_NAME, 8, 4, 26, 26, 0.2f);
    public static final Animation ANIMATION_LEFT_STAND = new Animation(ANIMATION_FILE_NAME, 8, 4, 27, 28, 0.2f);
    public static final Animation ANIMATION_STAND = ANIMATION_RIGHT_STAND;

    private static final float DENSITY = .1f;
    private static final float FRICTION = 0f;
    private static final float LINEAR_DAMPING = 0.8f;
    private static final float INPUT_FORCE_MULTIPLIER = 30f;
    private static final float JUMP_FORCE = 250f;
    private static final float SPRINT_VELOCITY_THRESHOLD = 6f;
    private static final float SPRINT_BURST = 50f;
    private static final float JUMP_SPRINT_FACTOR = 1.5f;

    private GroundContactHandler groundContactHandler;
    private Command commandChain;

    private boolean sprinting;
    private boolean alive = true;
    private boolean jumping;
    private Command commandChainOnDeath;

    public Player(float x, float y, Camera camera) {
        super(ANIMATION_STAND, BodyDef.BodyType.DynamicBody, x, y);
        this.commandChain = new HorizontalMovement(INPUT_FORCE_MULTIPLIER, camera)
            .setNext(new Jump(JUMP_FORCE, JUMP_SPRINT_FACTOR))
            .setNext(new CameraFollow(camera));
        this.commandChainOnDeath = new CameraFollow(camera);
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
            this.commandChain = this.commandChainOnDeath;
            TaskManager.INSTANCE.scheduleTimer("gameOverCountdown", 5,
                () -> TukeQuestGame.THIS.setScreen(new GameOverScreen(TukeQuestGame.THIS)));
        }
    }

    @Override
    public void act() {
        if (this.commandChain != null) {
            this.commandChain.execute(this);
        }
        if (canSprint()) {
            sprint();
        } else {
            stopSprintIfSlow();
        }
    }

    public boolean isOnGround() {
        return this.groundContactHandler.isHitting();
    }

    public boolean isSprinting() {
        return sprinting;
    }

    private boolean canSprint() {
        return !sprinting && isOnGround() && Math.abs(getBody().getLinearVelocity().x) > SPRINT_VELOCITY_THRESHOLD;
    }

    private void sprint() {
        float horizontalSpeed = getBody().getLinearVelocity().x;
        getBody().applyForceToCenter(new Vector2(
                Math.signum(horizontalSpeed) * SPRINT_BURST,
                0),
            true);
        sprinting = true;
        System.out.println(Player.class.getName() + " sprints");
    }

    private void stopSprintIfSlow() {
        if (sprinting && Math.abs(getBody().getLinearVelocity().x) < SPRINT_VELOCITY_THRESHOLD) {
            sprinting = false;
            System.out.println(Player.class.getName() + " no longer sprints");
        }
    }

    @Override
    protected void configureBodyDef(BodyDef bodyDef) {
        // Player slows down if the force isn't applied
        bodyDef.linearDamping = LINEAR_DAMPING;
        // Player cannot rotate
        bodyDef.fixedRotation = true;
    }

    @Override
    protected void addContactHandlers(MyContactListener contactListener, Fixture fixture) {
        super.addContactHandlers(contactListener, fixture);
        this.groundContactHandler = new GroundContactHandler(fixture);
        contactListener.addHandler(this.groundContactHandler);
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
    }

    public void collected(Collectable collectable) {
        if (collectable instanceof Surprise) {
            float force = getBody().getLinearVelocity().y <= 0 ? JUMP_FORCE * 4 : JUMP_FORCE;
            getBody().applyForceToCenter(new Vector2(0f, force), true);
        }
        collectable.collected(this);
    }

}
