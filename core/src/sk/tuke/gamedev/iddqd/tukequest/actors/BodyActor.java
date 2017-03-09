package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 08.03.2017.
 */
public interface BodyActor extends Actor {

    void addToWorld(World world);

    Animation getAnimation();

    void setAnimation(Animation animation);

    float getX();

    float getY();

    Vector2 getPosition();

    void setX(float x);

    void setY(float y);

    void setPosition(float x, float y);

    void setPosition(Vector2 position);

    float getRotation();

    void setRotation(float rotationDegrees);

    Body getBody();

}
