package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 09.03.2017.
 */
public class RectangleActor extends AbstractBodyActor {

    private float width;
    private float height;

    public RectangleActor(Animation animation, BodyDef.BodyType bodyType, float x, float y, float width, float height) {
        super(animation, bodyType, x, y);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Implicit animation size constructor.
     */
    public RectangleActor(Animation animation, BodyDef.BodyType bodyType, float x, float y) {
        this(animation, bodyType, x, y, animation.getWidth(), animation.getHeight());
    }

    @Override
    public void act() {
    }

    @Override
    protected Shape createShape(float scaleMultiplier) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(scaleMultiplier * this.width / 2, scaleMultiplier * this.height / 2, getCenterOffset().scl(scaleMultiplier), 0);
        return shape;
    }

    @Override
    public Vector2 getCenterOffset() {
        return new Vector2(this.width / 2, this.height / 2);
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        fixtureDef.density = 0f;
    }

}
