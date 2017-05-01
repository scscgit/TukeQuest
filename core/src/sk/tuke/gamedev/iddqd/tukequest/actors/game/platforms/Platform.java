package sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.managers.PlatformManager;
import sk.tuke.gamedev.iddqd.tukequest.managers.ScoreManager;
import sk.tuke.gamedev.iddqd.tukequest.screens.AbstractScreen;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.*;

public class Platform extends RectangleActor implements Ground {

    public enum PlatformTexture {

        MATHS("maths_texture.jpg"),
        BINARY("binary.jpg"),
        ROCK("jerusrockwallsml.jpg"),
        CHIMNEY("cabin_chimney_side.jpg");

        private String textureFileName;

        PlatformTexture(String s) {
            this.textureFileName = s;
        }

        public String getTextureFileName() {
            return textureFileName;
        }

    }

    private final static List<PlatformTexture> PLATFORM_TEXTURES = new ArrayList<>();
    public final static Map<PlatformSize, Map<PlatformTexture, Animation>> ANIMATIONS = new HashMap<>();

    private boolean scoreAwarded = false;

    static {
        // Add all enum values
        PLATFORM_TEXTURES.addAll(Arrays.asList(PlatformTexture.values()));

        // Generate animations in all sizes
        PlatformSize.getAll().forEach((platformSize -> {
            Map<PlatformTexture, Animation> animations = new HashMap<>();
            // for all textures
            PLATFORM_TEXTURES.forEach((platformTexture) -> {
                animations.put(platformTexture, new Animation(platformTexture.getTextureFileName(), 1f, platformSize.getPlatformWidth(), 24));
            });

            ANIMATIONS.put(platformSize, animations);
        }));

    }

    public Platform(float x, float y, PlatformSize platformSize, PlatformTexture texture) {
        super(
            ANIMATIONS.get(platformSize).get(texture),
            BodyDef.BodyType.StaticBody,
            x,
            y,
            ANIMATIONS.get(platformSize).get(texture).getWidth(),
            ANIMATIONS.get(platformSize).get(texture).getHeight()
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
        return PLATFORM_TEXTURES.size();
    }

    @Deprecated
    public void markAsScoreAwarded() {
        scoreAwarded = true;
        if (TukeQuestGame.debug) {
            setAnimation(Animation.INVISIBLE);
        }
    }

}
