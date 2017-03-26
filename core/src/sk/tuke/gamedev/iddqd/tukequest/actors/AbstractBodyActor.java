package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.ActorContactHandler;
import sk.tuke.gamedev.iddqd.tukequest.physics.contacts.MyContactListener;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 07.03.2017.
 */
public abstract class AbstractBodyActor extends AbstractAnimatedActor implements BodyActor {

    public static final float SCALE_FROM_PHYSICS = 1.02f;
    public static final float SCALE_TO_PHYSICS = 1 / SCALE_FROM_PHYSICS;

    private BodyType bodyType;
    private Body body;
    private ActorContactHandler actorContactHandler;
    private boolean settingPositionBlocked;

    protected AbstractBodyActor(Animation animation, BodyType bodyType, float x, float y) {
        super(animation, x, y);
        this.bodyType = bodyType;
        settingPositionBlocked = true;
    }

    @SuppressWarnings("unchecked")
    public final <T extends BodyActor> T addToWorld(AbstractScreen screen) {
        if (!screen.isAddingActor()) {
            // Go call the screen and get called back one more time
            return (T) screen.addActor(this);
        }
        this.body = createBody(this.bodyType, screen);
        return (T) this;
    }

    @Override
    public Vector2 getCenterOffset() {
        // Default implementation assumes the animation is set properly
        return new Vector2(getAnimation().getWidth() / 2, getAnimation().getHeight() / 2);
    }

    @Override
    public final Vector2 getCenter() {
        return getPosition().cpy().add(getCenterOffset());
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void setPositionFromBody() {
        this.settingPositionBlocked = false;
        setPosition(body.getPosition().cpy().scl(SCALE_FROM_PHYSICS));
        this.settingPositionBlocked = true;
    }

    @Override
    public void setPosition(float x, float y) {
        if (this.settingPositionBlocked) {
            throw new UnsupportedOperationException("Can not set position directly to the BodyActor");
        }
        super.setPosition(x, y);
    }

    @Override
    public boolean collides(BodyActor actor) {
        return this.actorContactHandler.collides(actor);
    }

    private Body createBody(BodyType bodyType, AbstractScreen screen) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;

        // Set our body's starting position in the world
        bodyDef.position.set(getX() * SCALE_TO_PHYSICS, getY() * SCALE_TO_PHYSICS);

        configureBodyDef(bodyDef);

        // Create our body in the world using our body definition
        Body body = screen.getWorld().createBody(bodyDef);

        // Create a shape and a fixture definition to apply our shape to
        addContactHandlers(screen.getWorldContactListener(), createFixture(createShape(SCALE_TO_PHYSICS), body));

        body.setUserData(this);
        return body;
    }

    // Maybe not needed, usually these properties can be accessed using {@Body} class too
    protected void configureBodyDef(BodyDef bodyDef) {
    }

    /**
     * Create a physical shape that will act as a collider.
     *
     * @param scaleMultiplier Size of the shape must be multiplied by this coefficient.
     * @return Created shape.
     */
    protected abstract Shape createShape(float scaleMultiplier);

    /**
     * Registration place for all contact Actor's handlers.
     *
     * @param contactListener Provides a way to add a new handler.
     * @param fixture         Give this important treasure to your handler.
     */
    protected void addContactHandlers(MyContactListener contactListener, Fixture fixture) {
        this.actorContactHandler = new ActorContactHandler(fixture);
        contactListener.addHandler(this.actorContactHandler);
    }

    private Fixture createFixture(Shape shape, Body body) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        configureFixtureDef(fixtureDef);

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        shape.dispose();
        return fixture;
    }

    protected abstract void configureFixtureDef(FixtureDef fixtureDef);

}
