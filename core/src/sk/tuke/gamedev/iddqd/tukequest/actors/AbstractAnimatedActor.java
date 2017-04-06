package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;
import sk.tuke.gamedev.iddqd.tukequest.visual.particles.Particle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 26.03.2017.
 */
public abstract class AbstractAnimatedActor implements AnimatedActor {

    private Animation animation;
    private float x;
    private float y;
    private float rotationDegrees;
    private List<Particle> particles;

    protected AbstractAnimatedActor(Animation animation, float x, float y) {
        setAnimation(animation);
        setPosition(x, y);
    }

    @Override
    public void draw(Batch batch) {
        this.animation.draw(batch, this);
        if (this.particles != null) {
            this.particles.forEach(particle -> particle.draw(batch, this));
        }
    }

    @Override
    public abstract void act();

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
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Convenience setter method instead of setX(x), setY(y).
     *
     * @param position Actor's axes
     */
    @Override
    public final void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    @Override
    public float getRotation() {
        return this.rotationDegrees;
    }

    @Override
    public void setRotation(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public void addParticle(Particle particle) {
        if (this.particles == null) {
            this.particles = new ArrayList<>();
        }
        this.particles.add(particle);
    }

}
