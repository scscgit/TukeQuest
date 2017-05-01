package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderFirst;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.assets.BackgroundTexture;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.assets.PlatformTexture;
import sk.tuke.gamedev.iddqd.tukequest.actors.game.platforms.PlatformSize;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steve on 26.03.2017.
 */
public class Background extends AbstractAnimatedActor implements RenderFirst {

    private static final Map<BackgroundTexture, Animation> animations = new HashMap<>();

    static {

        for (BackgroundTexture texture : BackgroundTexture.values()) {
            animations.put(texture, new Animation(texture.getTextureFileName(), Animation.ScaleType.SCALE_WIDTH, TukeQuestGame.SCREEN_WIDTH));
        }
    }

    public Background(float y, BackgroundTexture texture) {
        super(animations.get(texture), 0, y);
    }

    @Override
    public void act() {
    }

}
