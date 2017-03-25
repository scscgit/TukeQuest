package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

public class Platform extends RectangleActor {

    private final static Animation ANIMATION;

    static {
        // Scales the animation to the full screen width
        ANIMATION = new Animation("jerusrockwallsml.jpg", 1f, 128, 24);
    }

    // length specifies how many blocks the platform consists of
    public Platform(float x, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATION.getWidth(),
            ANIMATION.getHeight()
        );
    }

    @Override
    public void act() {
        super.act();
        if (PlatformManager.INSTANCE == null) {
            throw new RuntimeException(PlatformManager.class.getSimpleName() + " instance is not initialized");
        }
        getBody().setActive(PlatformManager.INSTANCE.isPlatformActive(this));
    }

}
