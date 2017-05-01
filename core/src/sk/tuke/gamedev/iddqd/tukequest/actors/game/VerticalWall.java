package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Vertical side wall with the theme of binary numbers.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public class VerticalWall extends RectangleActor {

    public static final int WALL_WIDTH = 40;

    private static final Map<WallTexture, Animation> animations = new HashMap<>();

    static {
        for (WallTexture texture : WallTexture.values()) {
            animations.put(texture, new Animation(texture.getTextureFileName(), 1, WALL_WIDTH, 452));
        }
    }

    public VerticalWall(Side side, float y, WallTexture texture) {
        super(
            animations.get(texture),
            BodyDef.BodyType.StaticBody,
            side == Side.LEFT ? 0 : TukeQuestGame.SCREEN_WIDTH - animations.get(texture).getWidth(),
            y
        );
    }

    @Override
    protected void configureFixtureDef(FixtureDef fixtureDef) {
        super.configureFixtureDef(fixtureDef);
        //fixtureDef.restitution = 0.6f;
    }

    public enum WallTexture {
        BINARY("binary_vertical_wall.jpg");

        private String textureFileName;

        WallTexture(String s) {
            this.textureFileName = s;
        }

        public String getTextureFileName() {
            return textureFileName;
        }
    }

    public enum Side {
        LEFT, RIGHT
    }


}
