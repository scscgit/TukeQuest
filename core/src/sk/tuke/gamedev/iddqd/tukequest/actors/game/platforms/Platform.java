package sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends RectangleActor implements Ground {

    private final static List<String> PLATFORM_TEXTURE_NAMES = new ArrayList<>();
    private final static Map<PlatformSize, List<Animation>> ANIMATIONS = new HashMap<>();

    private boolean scoreAwarded = false;

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
    protected void onAddToWorld(AbstractScreen screen) {
        super.onAddToWorld(screen);
        // Does not trigger active state switch on spawn
        getBody().setActive(false);
    }

    @Override
    public void act() {
        super.act();
        if (PlatformManager.INSTANCE == null) {
            throw new RuntimeException(PlatformManager.class.getSimpleName() + " instance is not initialized");
        }

        boolean wasActive = getBody().isActive();
        boolean isActive = PlatformManager.INSTANCE.isPlatformActive(this);
        getBody().setActive(isActive);

        // Only notify score manager if the state has changed and score wasn't awarded for this platform yet
        if (!scoreAwarded && wasActive != isActive) {
            ScoreManager.INSTANCE.notifyPlatformActiveChanged(this, isActive);
        }
    }

    public static int getPlatformTexturesCount() {
        return PLATFORM_TEXTURE_NAMES.size();
    }

    public void markAsScoreAwarded() {
        scoreAwarded = true;
        if (TukeQuestGame.debug) {
            setAnimation(Animation.INVISIBLE);
        }
    }

}
