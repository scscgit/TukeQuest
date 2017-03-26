package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;

/**
 * {@link Actor} that can be placed in a physical {@link World}.
 * <p>
 * Created by Steve on 08.03.2017.
 */
public interface BodyActor extends AnimatedActor {

    <T extends BodyActor> T addToWorld(AbstractScreen screen);

    Vector2 getCenterOffset();

    Vector2 getCenter();

    Body getBody();

    void setPositionFromBody();

    boolean collides(BodyActor actor);

}
