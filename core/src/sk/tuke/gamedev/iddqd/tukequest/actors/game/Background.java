package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderFirst;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

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

    public enum BackgroundTexture {
        ICY_TOWER("background.jpg");

        private String textureFileName;

        BackgroundTexture(String s) {
            this.textureFileName = s;
        }

        public String getTextureFileName() {
            return textureFileName;
        }
    }
}
