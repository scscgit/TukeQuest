package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 07.03.2017.
 */
public abstract class AbstractBodyActor implements BodyActor {

    private Animation animation;
    private BodyType bodyType;
    private float x;
    private float y;
    private float rotationDegrees;
    private World world;
    private Body body;

    protected AbstractBodyActor(Animation animation, BodyType bodyType, float x, float y) {
        setAnimation(animation);
        this.bodyType = bodyType;
        setPosition(x, y);
    }

    @Override
    public void draw(Batch batch) {
        this.animation.draw(batch, this);
    }

    @Override
    public abstract void act();

    @SuppressWarnings("unchecked")
    public final <T extends BodyActor> T addToWorld(World world) {
        if (this.world != null) {
            throw new UnsupportedOperationException("The Actor is already in a World");
        }
        this.world = world;
        this.body = createBody(this.bodyType, world);
        return (T) this;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    /**
     * Convenience getter method that converts axes from getX() and getY() into a Vector2.
     *
     * @return Actor's world coordinate axes
     */
    @Override
    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    /**
     * Convenience setter method instead of setX(x), setY(y).
     *
     * @param x Actor's X axis
     * @param y Actor's Y axis
     */
    @Override
    public final void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Convenience setter method instead of setX(x), setY(y).
     *
     * @param position Actor's axes
     */
    @Override
    public void setPosition(Vector2 position) {
        x = position.x;
        y = position.y;
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
    public float getRotation() {
        return this.rotationDegrees;
    }

    @Override
    public void setRotation(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public Body getBody() {
        return body;
    }

    private Body createBody(BodyType bodyType, World world) {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;

        // Set our body's starting position in the world
        bodyDef.position.set(getX(), getY());

        configureBodyDef(bodyDef);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Create a shape and a fixture definition to apply our shape to
        createFixture(createShape(), body);

        body.setUserData(this);
        return body;
    }

    protected void configureBodyDef(BodyDef bodyDef) {
    }

    protected abstract Shape createShape();

    private void createFixture(Shape shape, Body body) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        configureFixtureDef(fixtureDef);

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        shape.dispose();
    }

    protected abstract void configureFixtureDef(FixtureDef fixtureDef);

}
