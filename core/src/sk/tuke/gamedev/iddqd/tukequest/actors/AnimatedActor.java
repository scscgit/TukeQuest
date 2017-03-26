package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Visually represented game object mindful of space coordinates.
 * <p>
 * Created by Steve on 26.03.2017.
 */
public interface AnimatedActor extends Actor {

    Animation getAnimation();

    void setAnimation(Animation animation);

    float getX();

    float getY();

    Vector2 getPosition();

    void setPosition(float x, float y);

    void setPosition(Vector2 position);

    float getRotation();

    void setRotation(float rotationDegrees);

}
