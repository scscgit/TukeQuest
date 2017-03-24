package sk.tuke.gamedev.iddqd.tukequest.actors;


import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

public class Platform extends RectangleActor {

    private final static Animation ANIMATION;
    static {
        // Scales the animation to the full screen width
        ANIMATION = new Animation("jerusrockwallsml.jpg");
    }

    // length specifies how many blocks the platform consists of
    public Platform(float x, float y, int length) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATION.getWidth() * length,
            ANIMATION.getHeight()
        );
    }
}
