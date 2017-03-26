package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.List;

public class Platform extends RectangleActor implements Ground {

    public final static List<Animation> ANIMATIONS;

    static {
        // Scales the animation to the full screen width
//        ANIMATION = new Animation("jerusrockwallsml.jpg", 1f, 128, 24);
        ANIMATIONS = new ArrayList<>();
        ANIMATIONS.add(new Animation("maths_texture.jpg", 1f, 128, 24));
        ANIMATIONS.add(new Animation("binary_vertical_wall.jpg", 1f, 128, 24));
        ANIMATIONS.add(new Animation("jerusrockwallsml.jpg", 1f, 128, 24));
        ANIMATIONS.add(new Animation("cabin_chimney_side.jpg", 1f, 128, 24));
    }

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
