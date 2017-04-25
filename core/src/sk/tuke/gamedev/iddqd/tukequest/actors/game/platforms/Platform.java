package sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends RectangleActor implements Ground {

    private final static List<String> PLATFORM_TEXTURE_NAMES = new ArrayList<>();
    private final static Map<PlatformSize, List<Animation>> ANIMATIONS = new HashMap<>();

    public boolean scoreCollected = false;

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

        // if the player went above the platform, and score for this platform was not already counted, add score points
        // TODO: // FIXME: 25/04/2017 this logic adds score for platform that player is above, so the player does not have to actually jump / reach the platform

        if (!scoreCollected && getBody().isActive()) {
            // add to list the platforms the player went above
            ScoreManager.platformsInRow.add(this);
            scoreCollected = true;
            System.out.println("Added platform to Scoremanager.platformsInRow");
        }

    }

    public static int getPlatformTexturesCount() {
        return PLATFORM_TEXTURE_NAMES.size();
    }

}
