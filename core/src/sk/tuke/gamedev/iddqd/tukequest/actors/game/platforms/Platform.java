package sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms;


import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.Ground;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends RectangleActor implements Ground {

    private final static List<String> PLATFORM_TEXTURE_NAMES = new ArrayList<>();
    private final static Map<PlatformSize, List<Animation>> ANIMATIONS = new HashMap<>();

    static {
        // Scales the animation to the full screen width
//        ANIMATION = new Animation("jerusrockwallsml.jpg", 1f, 128, 24);
        PLATFORM_TEXTURE_NAMES.add("maths_texture.jpg");
        PLATFORM_TEXTURE_NAMES.add("binary_vertical_wall.jpg");
        PLATFORM_TEXTURE_NAMES.add("jerusrockwallsml.jpg");
        PLATFORM_TEXTURE_NAMES.add("cabin_chimney_side.jpg");


        // generate animations in all sizes
        PlatformSize.getAll().forEach((platformSize -> {
            List<Animation> animations = new ArrayList<>();
            // for all textures
            PLATFORM_TEXTURE_NAMES.forEach((platformTextureName) -> {
                animations.add(new Animation(platformTextureName, 1f, platformSize.getPlatformWidth(), 24));
            });

            ANIMATIONS.put(platformSize, animations);
        }));

    }

    public Platform(float x, float y, PlatformSize platformSize, int textureIndex) {
        super(
            ANIMATIONS.get(platformSize).get(textureIndex),
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATIONS.get(platformSize).get(textureIndex).getWidth(),
            ANIMATIONS.get(platformSize).get(textureIndex).getHeight()
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

    public static int getPlatformTexturesCount() {
        return PLATFORM_TEXTURE_NAMES.size();
    }

}
