package sk.tuke.gamedev.iddqd.tukequest.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Vertical side wall with the theme of binary numbers.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public class BinaryVerticalWall extends RectangleActor {

    public static final Animation ANIMATION = new Animation("binary_vertical_wall.jpg");

    private static final int SCREEN_WIDTH = TukeQuestGame.SCREEN_WIDTH;

    public enum Side {
        LEFT, RIGHT
    }

    public BinaryVerticalWall(Side side, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            side == Side.LEFT ? 0 : SCREEN_WIDTH - ANIMATION.getWidth(),
            y
        );
    }

}
