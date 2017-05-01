package sk.tuke.gamedev.iddqd.tukequest.actors.game.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FullScreenImage;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.FxFlame;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.VerticalWall;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.collectable.Collectable;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.CameraOnlyMovement;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Command;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.HorizontalMovement;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.player.commands.Jump;
import sk.tuke.gamedev.iddqd.tukequest.actors.strategy.CameraFollow;
import sk.tuke.gamedev.iddqd.tukequest.managers.TaskManager;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.OneTypeContactHandler;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameOverScreen;
import sk.tuke.gamedev.iddqd.tukequest.screens.GameScreen;
import sk.tuke.gamedev.iddqd.tukequest.util.Log;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.HUD;
import sk.tuke.gamedev.iddqd.tukequest.visual.particles.FlameFootParticle;

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

    public static final float NO_LONGER_WALKING_SPEED_THRESHOLD = 1;
    private static final float DENSITY = .1f;
    private static final float FRICTION = 0f;
    private static final float LINEAR_DAMPING = 1f;
    private static final float INPUT_FORCE_MULTIPLIER = 30f;
    private static final float JUMP_FORCE = 500f;
    private static final float RESURRECTION_FORCE = JUMP_FORCE * 2;
    private static final float SPRINT_VELOCITY_THRESHOLD = 9f;
    private static final float SPRINT_BURST = 50f;
    private static final float JUMP_SPRINT_FACTOR = 1.5f;
    private static final float WALL_HIT_HORIZONTAL_SPEED_PRESERVED = 0.75f;

    private OneTypeContactHandler groundContactHandler;
    private OneTypeContactHandler wallContactHandler;
    private Command commandChain;

    private int lives = 2;
    private boolean sprinting;
    private boolean alive = true;
    private boolean jumping;
    private Command commandChainOnDeath;
    private FlameFootParticle particle;
    private boolean wasHittingWall;
    private float horizontalSpeedInLastAct;
    private Camera camera;
    private boolean invulnerable;
    private float hitFlamesOnY;
    private GameScreen gameScreen;

    public Player(float x, float y, Camera camera, GameScreen gameScreen) {
        super(ANIMATION_STAND, BodyDef.BodyType.DynamicBody, x, y);
        this.commandChain = new HorizontalMovement(INPUT_FORCE_MULTIPLIER, camera)
            .setNext(new Jump(JUMP_FORCE, JUMP_SPRINT_FACTOR))
            .setNext(new CameraOnlyMovement(camera));
        this.commandChainOnDeath = new CameraOnlyMovement(camera);
        this.particle = new FlameFootParticle(this::isSprinting);
        this.camera = camera;
        this.gameScreen = gameScreen;
        addParticle(this.particle);
        getParticle().setImage(FlameFootParticle.ParticleImage.ASSEMBLER);
    }

    public FlameFootParticle getParticle() {
        return particle;
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

    public boolean isAlive() {
        return this.alive;
    }

    public void killedByFlame() {
        if (!isAlive() || this.invulnerable) {
            return;
        }
        if (this.lives > 1) {
            lostLife();
            return;
        }
        this.alive = false;
        Log.i(this, "Game Over");
        getBody().getFixtureList().get(0).setSensor(true);
        this.commandChain = this.commandChainOnDeath;
        TaskManager.INSTANCE.scheduleTimer("gameOverCountdown", 3,
            () -> TukeQuestGame.THIS.setScreen(new GameOverScreen(TukeQuestGame.THIS)));
        // Persistent flame always displayed on screen
        FullScreenImage persistentFlame = new FullScreenImage(FxFlame.ANIMATION, this.camera, 0, 0);
        persistentFlame.addStrategy(new CameraFollow(this.camera, persistentFlame));
        persistentFlame.setLastActOrder(1);
        getScreen().addActor(persistentFlame);
        // Does not jump above the persistent flame
        removeJumpForce();
    }

    private void lostLife() {
        this.lives--;
        Log.i(this, "Lost one life, " + this.lives + " remaining");
        removeJumpForce();
        getBody().applyForceToCenter(0, RESURRECTION_FORCE, true);
        this.invulnerable = true;
        this.hitFlamesOnY = getBody().getPosition().y;
        TaskManager.INSTANCE.scheduleTimer("playerVulnerable", 1, () -> {
            Log.d(this, "No longer invulnerable");
            this.invulnerable = false;
            // Double-check in case physics go wrong and let player slip below flames during invulnerability
            if (this.hitFlamesOnY > getBody().getPosition().y) {
                lostLife();
            }
        });
        this.gameScreen.getHud().setLives(this.lives);
        this.gameScreen.getFirstFlame().playerLostLife();
    }

    private void removeJumpForce() {
        float yVelocity = getBody().getLinearVelocity().y;
        getBody().setLinearVelocity(getBody().getLinearVelocity().x, yVelocity > 0 ? 0 : yVelocity);
    }

    @Override
    public void act() {
        // Executes all movement commands
        if (this.commandChain != null) {
            this.commandChain.execute(this);
        }

        // Receives a boost when hitting wall during a sprint
        if (this.wasHittingWall && !isHittingWall()) {
            this.wasHittingWall = false;
            Log.t(this, "No longer hitting the wall");
        } else if (!this.wasHittingWall && isHittingWall() && isSprinting()) {
            this.wasHittingWall = true;
            Log.d(this, "Hit the wall during sprint");
            // Neutralizes the downward fall (if any) and reverses the previous horizontal direction
            getBody().setLinearVelocity(new Vector2(
                -this.horizontalSpeedInLastAct * WALL_HIT_HORIZONTAL_SPEED_PRESERVED,
                getBody().getLinearVelocity().y > 0 ? getBody().getLinearVelocity().y : 0));
        }
        this.horizontalSpeedInLastAct = getBody().getLinearVelocity().x;

        if (canSprint()) {
            sprint();
        } else {
            stopSprintIfSlow();
        }
    }

    public boolean isOnGround() {
        return this.groundContactHandler.isHitting();
    }

    public boolean isHittingWall() {
        return this.wallContactHandler.isHitting();
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
        Log.i(this, "Sprints");
    }

    private void stopSprintIfSlow() {
        if (sprinting && Math.abs(getBody().getLinearVelocity().x) < SPRINT_VELOCITY_THRESHOLD) {
            sprinting = false;
            Log.i(this, "No longer sprints");
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
        this.groundContactHandler = new OneTypeContactHandler(fixture, Ground.class);
        this.wallContactHandler = new OneTypeContactHandler(fixture, VerticalWall.class);
        contactListener.addHandler(this.groundContactHandler);
        contactListener.addHandler(this.wallContactHandler);
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
    }

    public void collected(Collectable collectable) {
        if (!isAlive()) {
            return;
        }
        collectable.collected(this);
        float jumpForce = collectable.causesJump() * JUMP_FORCE;
        if (jumpForce > 0) {
            float force = getBody().getLinearVelocity().y <= 0 ? jumpForce * 2 : jumpForce;
            getBody().applyForceToCenter(new Vector2(0f, force), true);
        }
    }

    public void addLife() {
        this.lives++;
        this.gameScreen.getHud().setLives(this.lives);
    }

    @Override
    public int getRenderLastOrder() {
        // Does not draw over the flame
        return 2;
    }

    public HUD createHud() {
        CameraOnlyMovement.levelCameraOnPlayerPosition(this, this.camera);
        HUD hud = new HUD(this.camera);
        hud.setLives(this.lives);
        return hud;
    }

}
