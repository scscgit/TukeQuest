package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Vertical side wall with the theme of binary numbers.
 * <p>
 * Created by Steve on 24.03.2017.
 */
public class BinaryVerticalWall extends RectangleActor {

    // TODO: do not increase the size of walls this way, but move them around when camera moves
    public static final Animation ANIMATION = new Animation("binary_vertical_wall.jpg", 1, 40, 452);

    public enum Side {
        LEFT, RIGHT
    }

    public BinaryVerticalWall(Side side, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            side == Side.LEFT ? 0 : TukeQuestGame.SCREEN_WIDTH - ANIMATION.getWidth(),
            y
        );
    }

}