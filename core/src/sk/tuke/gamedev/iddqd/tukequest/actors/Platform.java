package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends RectangleActor {

    public final static List<Animation> ANIMATIONS;

    static {
        // Scales the animation to the full screen width
//        ANIMATION = new Animation("jerusrockwallsml.jpg", 1f, 128, 24);
        ANIMATIONS = new ArrayList<>();
        ANIMATIONS.add(new Animation("binary_vertical_wall.jpg", 1f, 128, 24));
        ANIMATIONS.add(new Animation("jerusrockwallsml.jpg", 1f, 128, 24));
        ANIMATIONS.add(new Animation("cabin_chimney_side.jpg", 1f, 128, 24));


    }

    // length specifies how many blocks the platform consists of
    public Platform(float x, float y, int texture) {
        super(
            ANIMATIONS.get(texture),
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATIONS.get(texture).getWidth(),
            ANIMATIONS.get(texture).getHeight()
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
