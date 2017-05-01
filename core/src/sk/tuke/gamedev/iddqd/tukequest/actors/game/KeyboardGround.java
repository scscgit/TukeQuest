package sk.tuke.gamedev.iddqd.tukequest.actors.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.actors.Ground;
import sk.tuke.gamedev.iddqd.tukequest.actors.RectangleActor;
import sk.tuke.gamedev.iddqd.tukequest.actors.RenderLast;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Steve on 24.03.2017.
 */
public class KeyboardGround extends RectangleActor implements RenderLast, Ground {

    public static final Animation ANIMATION;

    static {
        // Scales the animation to the full screen width
        ANIMATION = new Animation("keyboard_ground.jpg");
        ANIMATION.setScale(TukeQuestGame.SCREEN_WIDTH / ANIMATION.getWidth());
    }

    public KeyboardGround(float x, float y) {
        super(
            ANIMATION,
            BodyDef.BodyType.StaticBody,
            x,
            y
        );
    }

    @Override
    public int getRenderLastOrder() {
        // Is only in front of the initial spawned platform
        return 0;
    }

}
